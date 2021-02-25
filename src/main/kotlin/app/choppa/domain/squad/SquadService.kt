package app.choppa.domain.squad

import app.choppa.domain.account.Account
import app.choppa.domain.base.BaseService
import app.choppa.domain.member.Member
import app.choppa.domain.member.MemberService
import app.choppa.domain.squad.history.SquadMemberHistory
import app.choppa.domain.squad.history.SquadMemberHistoryService
import app.choppa.exception.EntityNotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest.of
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation.REPEATABLE_READ
import org.springframework.transaction.annotation.Transactional
import java.io.Serializable
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*

@Service
class SquadService(
    @Autowired private val squadRepository: SquadRepository,
    @Autowired private val memberService: MemberService,
    @Autowired private val squadMemberHistoryService: SquadMemberHistoryService,
) : BaseService<Squad> {
    override fun find(account: Account): List<Squad> = squadRepository
        .findAll()
        .ownedBy(account)
        .orElseThrow { throw EntityNotFoundException("No squads exist yet.") }

    override fun find(id: UUID, account: Account): Squad = squadRepository
        .findById(id)
        .orElseThrow { throw EntityNotFoundException("Squad with id [$id] does not exist.") }
        .verifyOwnership(account)

    override fun find(ids: List<UUID>, account: Account): List<Squad> = squadRepository
        .findAllById(ids)
        .ownedBy(account)
        .orElseThrow { throw EntityNotFoundException("No squads found with given ids.") }

    // NOTE(u-ways) #149 Squad also allows the persistence of non-existent members on top of
    //                   persisting existing members from/to the squad's current members table.
    @Transactional(isolation = REPEATABLE_READ)
    override fun save(entity: Squad, account: Account): Squad = squadRepository.findById(entity.id)
        .getMembersIfPresent()
        .run {
            memberService.save(entity.members, account)
            squadRepository.save(
                squadRepository
                    .findById(entity.id)
                    .verifyOriginalOwnership(entity, account)
            ).createSquadMembersRevision(this)
        }

    @Transactional(isolation = REPEATABLE_READ)
    override fun save(entities: List<Squad>, account: Account): List<Squad> = entities
        .map { this.save(it, account) }

    @Transactional
    override fun delete(entity: Squad, account: Account): Squad = entity.apply {
        // Update members with 0 squad assignment to inactive.
        this.members.forEach {
            if (findRelatedByMember(it.id, account).size == 1) {
                memberService.save(it.copy(active = false), account)
            }
        }

        squadRepository.findById(entity.id).run {
            this.orElseGet { throw EntityNotFoundException("Squad with id [${entity.id}] does not exist.") }
                .verifyOwnership(account).also {
                    squadMemberHistoryService.deleteAllFor(entity)
                    squadRepository.deleteAllSquadMemberRecordsFor(entity.id)
                    squadRepository.delete(entity)
                }
        }
    }

    @Transactional
    override fun delete(entities: List<Squad>, account: Account): List<Squad> = entities
        .map { this.delete(it, account) }

    fun deleteRelatedByTribe(tribeId: UUID, account: Account): List<Squad> = squadRepository
        .findAllByTribeId(tribeId)
        .ownedBy(account)
        .run { delete(this, account) }

    fun findRelatedByMember(memberId: UUID, account: Account): List<Squad> = squadRepository
        .findAllByMemberId(memberId)
        .ownedBy(account)
        .orElseThrow { throw EntityNotFoundException("No squads found joined by member [$memberId] yet.") }

    fun findRelatedByTribe(tribeId: UUID, account: Account): List<Squad> = squadRepository
        .findAllByTribeId(tribeId)
        .ownedBy(account)
        .orElseThrow { throw EntityNotFoundException("No squads found belonging to tribe [$tribeId] yet.") }

    @Transactional
    fun findAllSquadMembersRevisions(id: UUID, account: Account) = find(id, account).run {
        this to squadMemberHistoryService.concentrateAllSquadRevisions(this)
    }

    @Transactional
    fun findLastNSquadMembersRevisions(id: UUID, revisionAmount: Int, account: Account) = find(id, account).run {
        this to (1..revisionAmount).map {
            squadMemberHistoryService.concentrateLastNSquadRevisions(this, it)
        }
    }

    fun calculateKspForLastNRevisionsFor(id: UUID, amount: Int): HashMap<String, Any> =
        (1..amount).fold(HashMap<String, Any>(amount)) { acc, i ->
            acc.also {
                acc[i.toString()] = mapOf(
                    "timestamp" to Instant.now().minus(i * 7L, ChronoUnit.DAYS).toEpochMilli(),
                    "ksp" to (200 - (i..200).random()),
                )
            }
        }

    fun statistics(account: Account): HashMap<String, Serializable> = squadRepository.findAll().ownedBy(account).run {
        val revisions = squadMemberHistoryService.runCatching {
            this.find(account, of(0, 20))
        }.getOrElse { emptyList() }
        hashMapOf(
            "total" to this.size,
            "latestChanges" to revisions.foldIndexed(HashMap<Int, SquadMemberHistory>(this.size)) { index, acc, revision ->
                acc.also {
                    acc[index + 1] = revision
                }
            }
        )
    }

    private fun Optional<Squad>.verifyOriginalOwnership(entity: Squad, account: Account): Squad =
        if (this.isPresent) entity.copy(account = this.get().account).verifyOwnership(account)
        else entity.copy(account = account)

    private fun Optional<Squad>.getMembersIfPresent() = when {
        this.isPresent -> this.get().members.toMutableList()
        else -> emptyList()
    }

    private fun Squad.createSquadMembersRevision(olderFormation: List<Member>) = this.apply {
        squadMemberHistoryService.save(
            squadMemberHistoryService.generateRevisions(this, olderFormation)
        )
    }
}
