package app.choppa.domain.squad

import app.choppa.domain.account.AccountService
import app.choppa.domain.base.BaseService
import app.choppa.domain.member.Member
import app.choppa.domain.member.MemberService
import app.choppa.domain.rotation.smr.MemberPairingPoints
import app.choppa.domain.rotation.smr.SquadPairingPoints
import app.choppa.domain.squad.history.SquadMemberHistory
import app.choppa.domain.squad.history.SquadMemberHistoryService
import app.choppa.exception.EntityNotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest.of
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation.REPEATABLE_READ
import org.springframework.transaction.annotation.Transactional
import java.io.Serializable
import java.util.*

@Service
class SquadService(
    @Autowired private val squadRepository: SquadRepository,
    @Autowired private val memberService: MemberService,
    @Autowired private val squadMemberHistoryService: SquadMemberHistoryService,
    @Autowired private val accountService: AccountService
) : BaseService<Squad>(accountService) {

    override fun find(sort: Sort): List<Squad> = squadRepository
        .findAll()
        .ownedByAuthenticated()
        .orElseThrow { throw EntityNotFoundException("No squads exist yet.") }

    override fun find(id: UUID, sort: Sort): Squad = squadRepository
        .findById(id)
        .orElseThrow { throw EntityNotFoundException("Squad with id [$id] does not exist.") }
        .verifyAuthenticatedOwnership()
        .sortByMembersChapterThenMembersName()

    override fun find(ids: List<UUID>, sort: Sort): List<Squad> = squadRepository
        .findAllById(ids)
        .ownedByAuthenticated()
        .orElseThrow { throw EntityNotFoundException("No squads found with given ids.") }
        .map { it.sortByMembersChapterThenMembersName() }

    // NOTE(u-ways) #149 Squad also allows the persistence of non-existent members on top of
    //                   persisting existing members from/to the squad's current members table.
    @Transactional(isolation = REPEATABLE_READ)
    override fun save(entity: Squad): Squad = squadRepository.findById(entity.id)
        .getMembersIfPresent()
        .setRemovedMembersToInactive(entity.members)
        .run {
            memberService.save(entity.members.setAllToActive())
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
        .map { it.sortByMembersChapterThenMembersName() }

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

    @Transactional
    fun findSquadsRevisionsAndMemberDurations(squads: List<Squad>): List<Pair<Squad, List<Pair<Int, List<Member>>>>> =
        squadMemberHistoryService.findSquadsRevisionsAndMemberDurations(squads)

    fun calculateKspForLastNRevisionsFor(
        id: UUID,
        mpp: MemberPairingPoints,
        spp: SquadPairingPoints
    ): Double = find(id).members.map { mem ->
        val memSpp = spp.filter { x -> x.key.member == mem }
        val sppScalar = memSpp.entries.minByOrNull { x -> x.value }?.let { it.value / it.key.squad.members.size } ?: 0
        val maxSPP = memSpp.entries.sumBy { x -> x.key.squad.members.size } * sppScalar
        val currentSPP = memSpp.entries.sumBy { x -> x.value }
        val finalSPP = if (maxSPP != 0) (currentSPP.toDouble() / maxSPP.toDouble()) else 0.0
        val memMpp = mpp.filter { x -> x.key.member_1 == mem || x.key.member_2 == mem }
        val maxMPP = memMpp.minByOrNull { x -> x.value }?.value?.times(memMpp.entries.count()) ?: 0
        val currentMPP = memMpp.entries.sumBy { x -> x.value }
        val finalMPP = if (maxMPP != 0) (currentMPP.toDouble() / maxMPP.toDouble()) else 0.0
        (finalSPP * 0.9f) + (finalMPP * 0.1f)
    }.average()

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

    private fun List<Member>.setRemovedMembersToInactive(updatedMember: List<Member>) = apply {
        minus(intersect(updatedMember)).forEach { member ->
            // Only update members with 0 squad assignment to inactive.
            if (findRelatedByMember(member.id).size == 1) {
                memberService.save(member.copy(active = false))
            }
        }
    }

    private fun Squad.createSquadMembersRevision(olderFormation: List<Member>) = this.apply {
        squadMemberHistoryService.save(
            squadMemberHistoryService.generateRevisions(this, olderFormation)
        )
    }

    private fun MutableList<Member>.setAllToActive(): MutableList<Member> =
        map { it.copy(active = true) }.toMutableList()

    private fun Squad.sortByMembersChapterThenMembersName(): Squad = run {
        copy(members = members.sortedWith(compareBy({ it.chapter.name }, { it.name })).toMutableList())
    }
}
