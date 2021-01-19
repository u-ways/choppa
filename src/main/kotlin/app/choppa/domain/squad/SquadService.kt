package app.choppa.domain.squad

import app.choppa.domain.base.BaseService
import app.choppa.domain.member.Member
import app.choppa.domain.member.MemberService
import app.choppa.domain.squad.history.RevisionType.ADD
import app.choppa.domain.squad.history.RevisionType.REMOVE
import app.choppa.domain.squad.history.SquadMemberHistory
import app.choppa.domain.squad.history.SquadMemberHistoryService
import app.choppa.exception.EntityNotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class SquadService(
    @Autowired private val squadRepository: SquadRepository,
    @Autowired private val memberService: MemberService,
    @Autowired private val squadMemberHistoryService: SquadMemberHistoryService,
) : BaseService<Squad> {
    override fun find(): List<Squad> = squadRepository
        .findAll()
        .orElseThrow { throw EntityNotFoundException("No squads exist yet.") }

    override fun find(id: UUID): Squad = squadRepository
        .findById(id)
        .orElseThrow { throw EntityNotFoundException("Squad with id [$id] does not exist.") }

    override fun find(ids: List<UUID>): List<Squad> = squadRepository
        .findAllById(ids)
        .orElseThrow { throw EntityNotFoundException("No squads found with given ids.") }

    // NOTE(u-ways) #149 Squad also allows the persistence of non-existent members on top of
    //                   persisting existing members from/to the squad's current members table.
    @Transactional
    override fun save(entity: Squad): Squad = memberService
        .save(entity.members)
        .run { squadRepository.save(entity) }

    @Transactional
    override fun save(entities: List<Squad>): List<Squad> = entities.map(::save)

    override fun delete(entities: List<Squad>) = entities
        .apply { squadRepository.deleteAll(entities) }

    override fun delete(entity: Squad): Squad = entity
        .apply { squadRepository.delete(entity) }

    fun findRelatedByMember(memberId: UUID): List<Squad> = squadRepository
        .findAllByMemberId(memberId)
        .orElseThrow { throw EntityNotFoundException("No squads found joined by member [$memberId] yet.") }

    fun findRelatedByTribe(tribeId: UUID): List<Squad> = squadRepository
        .findAllByTribeId(tribeId)
        .orElseThrow { throw EntityNotFoundException("No squads found belonging to tribe [$tribeId] yet.") }

    @Transactional
    fun findAllSquadMembersRevisions(id: UUID): List<List<Member>> = find(id).run {
        squadMemberHistoryService
            .findAllSquadMemberRevisions(this)
            .foldRight(listOf()) { record: SquadMemberHistory, accumulator: List<List<Member>> ->
                when (accumulator.size) {
                    0 ->
                        accumulator
                            .plusElement(listOf(memberService.find(record.member.id)))
                    else ->
                        accumulator
                            .dropLastIf(accumulator.size != record.revisionNumber)
                            .plusElement(
                                when (record.revisionType) {
                                    ADD -> accumulator.last().plus(memberService.find(record.member.id))
                                    REMOVE -> accumulator.last().minus(record.member)
                                }
                            )
                }
            }
    }

    @Transactional
    fun revertSquadMembersFormationTo(id: UUID, revisionNumber: Int): Squad = find(id).let {
        it.copy(
            members = squadMemberHistoryService
                .findSquadMemberRevisionsAfter(it, revisionNumber)
                .foldRevisionsBy(it.members)
        )
    }

    @Transactional
    fun revertSquadMembersFormationBy(id: UUID, revisionAmount: Int): Squad = find(id).let {
        it.copy(
            members = squadMemberHistoryService
                .findLastNSquadMemberRevisions(it, revisionAmount)
                .foldRevisionsBy(it.members)
        )
    }

    private fun List<SquadMemberHistory>.foldRevisionsBy(members: List<Member>) = this
        .fold(members) { acc, record ->
            when (record.revisionType) {
                ADD -> acc.minus(record.member)
                REMOVE -> acc.plus(memberService.find(record.member.id))
            }
        }.toMutableList()

    private fun List<List<Member>>.dropLastIf(predicate: Boolean): List<List<Member>> =
        if (predicate) this.dropLast(1) else this
}
