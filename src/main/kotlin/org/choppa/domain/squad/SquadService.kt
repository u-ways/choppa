package org.choppa.domain.squad

import org.choppa.domain.base.BaseService
import org.choppa.domain.member.MemberService
import org.choppa.domain.tribe.TribeService
import org.choppa.exception.EntityNotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class SquadService(
    @Autowired private val squadRepository: SquadRepository,
    @Autowired private val tribeService: TribeService,
    @Autowired private val memberService: MemberService,
) : BaseService() {
    fun find(id: UUID): Squad = squadRepository
        .findById(id)
        .orElseThrow { throw EntityNotFoundException("Squad with id [$id] does not exist.") }

    fun find(): List<Squad> = squadRepository
        .findAll()
        .orElseThrow { throw EntityNotFoundException("No squads exist yet.") }

    fun findRelatedByMember(memberId: UUID): List<Squad> = squadRepository
        .findAllByMemberId(memberId)
        .orElseThrow { throw EntityNotFoundException("No squads found joined by member [$memberId] yet.") }

    fun findRelatedByTribe(tribeId: UUID): List<Squad> = squadRepository
        .findAllByTribeId(tribeId)
        .orElseThrow { throw EntityNotFoundException("No squads found belonging to tribe [$tribeId] yet.") }

    fun find(ids: List<UUID>): List<Squad> = squadRepository
        .findAllById(ids)
        .orElseThrow { throw EntityNotFoundException("No squads found with given ids.") }

    // NOTE(u-ways) #55 Squad is the owning map of tribe and members.
    //                  Therefore, service ensures they exist before relating tribe and members accordingly.
    //                  This avoids invalid foreign key inserts.
    @Transactional
    fun save(squad: Squad): Squad {
        val tribe = tribeService.find(squad.tribe.id)
        val members = memberService.find(squad.members.map { it.id }).apply {
            if (this.size != squad.members.size) {
                throw EntityNotFoundException("Persist operation on current squad members detected a member that doesn't exist in the database.")
            }
        }.toMutableList()

        return squadRepository.save(Squad(squad.id, squad.name, squad.color, tribe, members))
    }

    @Transactional
    fun save(squads: List<Squad>): List<Squad> {
        return squads.map(::save)
    }

    @Transactional
    fun save(vararg squads: Squad): List<Squad> {
        return save(squads.toMutableList())
    }

    fun delete(squad: Squad): Squad {
        squadRepository.delete(squad)
        return squad
    }

    fun delete(squads: List<Squad>): List<Squad> {
        squadRepository.deleteAll(squads)
        return squads
    }

    fun delete(vararg squads: Squad): List<Squad> {
        return delete(squads.toMutableList())
    }
}
