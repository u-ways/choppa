package app.choppa.domain.squad

import app.choppa.domain.account.AccountService
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
    @Autowired private val accountService: AccountService
) : BaseService<Squad>(accountService) {

    override fun find(): List<Squad> = squadRepository
        .findAll()
        .ownedByAuthenticated()
        .orElseThrow { throw EntityNotFoundException("No squads exist yet.") }

    override fun find(id: UUID): Squad = squadRepository
        .findById(id)
        .orElseThrow { throw EntityNotFoundException("Squad with id [$id] does not exist.") }
        .verifyAuthenticatedOwnership()

    override fun find(ids: List<UUID>): List<Squad> = squadRepository
        .findAllById(ids)
        .ownedByAuthenticated()
        .orElseThrow { throw EntityNotFoundException("No squads found with given ids.") }

    // NOTE(u-ways) #149 Squad also allows the persistence of non-existent members on top of
    //                   persisting existing members from/to the squad's current members table.
    @Transactional(isolation = REPEATABLE_READ)
    override fun save(entity: Squad): Squad = squadRepository.findById(entity.id)
        .getMembersIfPresent()
        .run {
            memberService.save(entity.members)
            squadRepository.save(
                squadRepository
                    .findById(entity.id)
                    .verifyOriginalOwnership(entity)
            ).createSquadMembersRevision(this)
        }

    @Transactional(isolation = REPEATABLE_READ)
    override fun save(entities: List<Squad>): List<Squad> = entities
        .map { this.save(it) }

    @Transactional
    override fun delete(entity: Squad): Squad = entity.apply {
        // Update members with 0 squad assignment to inactive.
        this.members.forEach {
            if (findRelatedByMember(it.id).size == 1) {
                memberService.save(it.copy(active = false))
            }
        }

        squadRepository.findById(entity.id).run {
            this.orElseGet { throw EntityNotFoundException("Squad with id [${entity.id}] does not exist.") }
                .verifyAuthenticatedOwnership().also {
                    squadMemberHistoryService.deleteAllFor(entity)
                    squadRepository.deleteAllSquadMemberRecordsFor(entity.id)
                    squadRepository.delete(entity)
                }
        }
    }

    @Transactional
    override fun delete(entities: List<Squad>): List<Squad> = entities
        .map { this.delete(it) }

    fun deleteRelatedByTribe(tribeId: UUID): List<Squad> = squadRepository
        .findAllByTribeId(tribeId)
        .ownedByAuthenticated()
        .run { delete(this) }

    fun findRelatedByMember(memberId: UUID): List<Squad> = squadRepository
        .findAllByMemberId(memberId)
        .ownedByAuthenticated()
        .orElseThrow { throw EntityNotFoundException("No squads found joined by member [$memberId] yet.") }

    fun findRelatedByTribe(tribeId: UUID): List<Squad> = squadRepository
        .findAllByTribeId(tribeId)
        .ownedByAuthenticated()
        .orElseThrow { throw EntityNotFoundException("No squads found belonging to tribe [$tribeId] yet.") }

    @Transactional
    fun findAllSquadMembersRevisions(id: UUID) = find(id).run {
        this to squadMemberHistoryService.concentrateAllSquadRevisions(this)
    }

    @Transactional
    fun findLastNSquadMembersRevisions(id: UUID, revisionAmount: Int) = find(id).run {
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

    fun statistics(): HashMap<String, Serializable> = squadRepository.findAll().ownedByAuthenticated().run {
        val revisions = squadMemberHistoryService.runCatching {
            this.find(of(0, 20))
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

    private fun Optional<Squad>.verifyOriginalOwnership(entity: Squad): Squad =
        if (this.isPresent) entity.copy(account = this.get().account).verifyAuthenticatedOwnership()
        else entity.copy(account = accountService.resolveFromAuth())

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
