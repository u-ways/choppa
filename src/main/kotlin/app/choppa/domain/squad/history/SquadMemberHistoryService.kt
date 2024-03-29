package app.choppa.domain.squad.history

import app.choppa.domain.account.AccountService
import app.choppa.domain.member.Member
import app.choppa.domain.member.MemberRepository
import app.choppa.domain.squad.Squad
import app.choppa.domain.squad.history.RevisionType.ADD
import app.choppa.domain.squad.history.RevisionType.REMOVE
import app.choppa.exception.EntityNotFoundException
import org.hibernate.type.IntegerType.ZERO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest.of
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Pageable.unpaged
import org.springframework.data.domain.Sort
import org.springframework.data.domain.Sort.Direction.DESC
import org.springframework.data.domain.Sort.by
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation.REPEATABLE_READ
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Service
class SquadMemberHistoryService(
    @Autowired private val squadHistoryRepository: SquadMemberHistoryRepository,
    @Autowired private val memberRepository: MemberRepository,
    @Autowired private val accountService: AccountService
) {
    fun find(pageable: Pageable = unpaged()): Page<SquadMemberHistory> = squadHistoryRepository
        .findAllByAccountIdOrderByCreateDateDesc(accountService.resolveFromAuth().id, pageable)
        .orElseThrow { throw EntityNotFoundException("No Squad Member History records exist yet.") }

    fun findBySquad(
        squad: Squad,
        sort: Sort = by(DESC, SquadMemberHistory::createDate.name)
    ): List<SquadMemberHistory> = squadHistoryRepository
        .findAllBySquad(squad, sort)

    fun findByMember(
        member: Member,
        sort: Sort = by(DESC, SquadMemberHistory::createDate.name)
    ): List<SquadMemberHistory> = squadHistoryRepository
        .findAllByMember(member, sort)

    fun save(entity: SquadMemberHistory): SquadMemberHistory = squadHistoryRepository
        .save(entity)

    fun save(entities: List<SquadMemberHistory>): List<SquadMemberHistory> = squadHistoryRepository
        .saveAll(entities)

    fun deleteAllFor(member: Member) = member
        .apply { squadHistoryRepository.deleteAllByMember(member) }

    fun deleteAllFor(squad: Squad) = squad
        .apply { squadHistoryRepository.deleteAllBySquad(squad) }

    fun delete(entity: SquadMemberHistory): SquadMemberHistory = entity
        .apply { squadHistoryRepository.delete(entity) }

    fun delete(entities: List<SquadMemberHistory>): List<SquadMemberHistory> = entities
        .apply { squadHistoryRepository.deleteAll(entities) }

    @Transactional
    fun generateRevisions(squad: Squad, olderFormation: List<Member>) = with(nextRevisionNumber(squad)) {
        differentiate(squad.members, olderFormation).map { (member, revisionType) ->
            SquadMemberHistory(squad, member, this, revisionType)
        }
    }

    @Transactional
    fun concentrateAllSquadRevisions(squad: Squad): List<List<Member>> = squadHistoryRepository
        .findAllBySquad(squad)
        .foldRight(listOf()) { record: SquadMemberHistory, accumulator: List<List<Member>> ->
            when (accumulator.size) {
                0 ->
                    accumulator
                        .plusElement(listOf(memberRepository.findById(record.member.id).get()))
                else ->
                    accumulator
                        .dropLastIf(accumulator.size != record.revisionNumber)
                        .plusElement(
                            when (record.revisionType) {
                                ADD -> accumulator.last().plus(memberRepository.findById(record.member.id).get())
                                REMOVE -> accumulator.last().minus(record.member)
                            }
                        )
            }
        }

    @Transactional(isolation = REPEATABLE_READ)
    fun concentrateSquadRevisionsTo(squad: Squad, revisionNumber: Int): List<Member> = squadHistoryRepository
        .findAllBySquadAndRevisionNumberAfter(squad, revisionNumber)
        .backtrack(squad.members)

    @Transactional(isolation = REPEATABLE_READ)
    fun concentrateLastNSquadRevisions(squad: Squad, revisionAmount: Int): List<Member> = squadHistoryRepository
        .takeIf { revisionAmount > 0 }
        ?.findAllBySquad(squad, of(ZERO, revisionAmount))
        ?.backtrack(squad.members) ?: emptyList()

    @Transactional
    fun findSquadsRevisionsAndMemberDurations(squads: List<Squad>): List<Pair<Squad, List<Pair<Int, List<Member>>>>> {
        val squadHistoriesList: List<List<SquadMemberHistory>> = squads.map {
            squadHistoryRepository.findAllBySquad(it, by(Sort.Direction.ASC, SquadMemberHistory::createDate.name))
        }

        val durations = squadHistoriesList.map {
            it.foldRight(listOf()) { record: SquadMemberHistory, accumulator: List<Pair<Instant, List<Member>>> ->
                when (accumulator.size) {
                    0 ->
                        accumulator
                            .plusElement(
                                Pair(
                                    record.createDate,
                                    listOf(memberRepository.findById(record.member.id).get())
                                )
                            )
                    else ->
                        accumulator
                            .plusElement(
                                Pair(
                                    record.createDate,
                                    when (record.revisionType) {
                                        ADD -> accumulator.last().second.plus(
                                            listOf(
                                                memberRepository.findById(record.member.id).get()
                                            )
                                        )
                                        REMOVE -> accumulator.last().second.minus(record.member)
                                    }
                                )
                            )
                }
            }
        }

        val squadsAndDurations = durations.map {
            var lastRecord = it[0]
            it.foldRight(listOf()) { current: Pair<Instant, List<Member>>, accumulator: List<Pair<Int, List<Member>>> ->
                when {
                    lastRecord != current -> accumulator.run {
                        plusElement(
                            Pair(
                                timestampDifferenceInDays(lastRecord.first, current.first),
                                lastRecord.second
                            )
                        ).apply { lastRecord = current }
                    }
                    accumulator.size == it.size - 1 -> accumulator.run {
                        plusElement(
                            Pair(
                                timestampDifferenceInDays(current.first, Instant.now()),
                                current.second
                            )
                        ).apply { lastRecord = current }
                    }
                    else -> accumulator
                }
            }
        }
        return squads.mapIndexed { index, squad -> Pair(squad, squadsAndDurations[index]) }
    }

    private fun timestampDifferenceInDays(start: Instant, end: Instant): Int {
        val millisToDays = 1000 * 60 * 60 * 24
        return (start.minusMillis(end.toEpochMilli()).toEpochMilli() / millisToDays).toInt()
    }

    private fun nextRevisionNumber(squad: Squad) = squadHistoryRepository
        .findAllBySquad(
            squad,
            of(ZERO, 1, by(DESC, SquadMemberHistory::revisionNumber.name))
        ).let {
            if (it.isEmpty()) ZERO else it.first().revisionNumber + 1
        }

    private fun differentiate(newerFormation: List<Member>, olderFormation: List<Member>) =
        with(newerFormation.intersect(olderFormation)) {
            newerFormation
                .minus(this)
                .map { Pair(it, ADD) }
                .plus(
                    olderFormation
                        .minus(this)
                        .map { Pair(it, REMOVE) }
                )
        }

    private fun List<SquadMemberHistory>.backtrack(members: List<Member>) = this
        .fold(members) { acc, record ->
            when (record.revisionType) {
                ADD -> acc.minus(record.member)
                REMOVE -> acc.plus(memberRepository.findById(record.member.id).get())
            }
        }.toMutableList()

    private fun <T> List<T>.dropLastIf(predicate: Boolean): List<T> =
        if (predicate) this.dropLast(1) else this

    private fun <T> Page<T>.orElseThrow(exception: () -> Nothing): Page<T> = when {
        this.isEmpty -> exception.invoke()
        else -> this
    }
}
