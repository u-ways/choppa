package app.choppa.domain.squad

import app.choppa.domain.base.BaseService
import app.choppa.domain.member.Member
import app.choppa.domain.member.MemberService
import app.choppa.domain.squad.history.SquadMemberHistory
import app.choppa.domain.squad.history.SquadMemberHistoryService
import app.choppa.exception.EntityNotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest.of
import org.springframework.data.domain.Sort.by
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
    @Transactional(isolation = REPEATABLE_READ)
    override fun save(entity: Squad): Squad = squadRepository
        .findById(entity.id)
        .getMembersIfPresent()
        .run {
            memberService.save(entity.members)
            squadRepository
                .save(entity)
                .createSquadMembersRevision(this)
        }

    @Transactional(isolation = REPEATABLE_READ)
    override fun save(entities: List<Squad>): List<Squad> = entities.map(::save)

    @Transactional
    override fun delete(entity: Squad): Squad = entity.apply {
        // Update members with 0 squad assignment to inactive.
        this.members.forEach {
            if (findRelatedByMember(it.id).size == 1) {
                memberService.save(it.copy(active = false))
            }
        }
        squadMemberHistoryService.deleteAllFor(entity)
        squadRepository.deleteAllSquadMemberRecordsFor(entity.id)
        squadRepository.delete(this)
    }

    @Transactional
    override fun delete(entities: List<Squad>): List<Squad> = entities.map(::delete)

    fun deleteRelatedByTribe(tribeId: UUID): List<Squad> = squadRepository
        .findAllByTribeId(tribeId)
        .run { delete(this) }

    fun findRelatedByMember(memberId: UUID): List<Squad> = squadRepository
        .findAllByMemberId(memberId)
        .orElseThrow { throw EntityNotFoundException("No squads found joined by member [$memberId] yet.") }

    fun findRelatedByTribe(tribeId: UUID): List<Squad> = squadRepository
        .findAllByTribeId(tribeId)
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

    private fun Optional<Squad>.getMembersIfPresent() = when {
        this.isPresent -> this.get().members.toMutableList()
        else -> emptyList()
    }

    private fun Squad.createSquadMembersRevision(olderFormation: List<Member>) = this.apply {
        squadMemberHistoryService.save(
            squadMemberHistoryService.generateRevisions(this, olderFormation)
        )
    }

    fun statistics(): HashMap<String, Serializable> = squadRepository.findAll().run {
        val revisions = squadMemberHistoryService.runCatching {
            this.find(of(0, 20, by(SquadMemberHistory::createDate.name).descending()))
        }.getOrElse { emptyList() }
        hashMapOf(
            "Total" to this.size,
            "Latest Changes" to revisions.foldIndexed(HashMap<Int, SquadMemberHistory>(this.size)) { index, acc, revision ->
                acc.also {
                    acc[index + 1] = revision
                }
            }
        )
    }
}
