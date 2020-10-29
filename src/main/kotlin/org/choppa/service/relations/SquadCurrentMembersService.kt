package org.choppa.service.relations

import org.choppa.model.relations.SquadCurrentMembers
import org.choppa.model.relations.SquadCurrentMembersId
import org.choppa.repository.relations.SquadCurrentMembersRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class SquadCurrentMembersService(
    @Autowired private val squadCurrentMembersRepository: SquadCurrentMembersRepository,
) {
    fun find(id: SquadCurrentMembersId): SquadCurrentMembers? {
        return squadCurrentMembersRepository.findById(id).orElseGet { null }
    }

    fun find(): List<SquadCurrentMembers> {
        return squadCurrentMembersRepository.findAll()
    }

    fun find(ids: List<SquadCurrentMembersId>): List<SquadCurrentMembers> {
        return squadCurrentMembersRepository.findAllById(ids)
    }

    fun save(squadCurrentMembers: SquadCurrentMembers): SquadCurrentMembers {
        return squadCurrentMembersRepository.save(squadCurrentMembers)
    }

    fun save(squadCurrentMembers: List<SquadCurrentMembers>): List<SquadCurrentMembers> {
        return squadCurrentMembersRepository.saveAll(squadCurrentMembers)
    }

    fun save(vararg squadCurrentMembers: SquadCurrentMembers): List<SquadCurrentMembers> {
        return save(squadCurrentMembers.toMutableList())
    }

    fun delete(squadCurrentMembers: SquadCurrentMembers): SquadCurrentMembers {
        squadCurrentMembersRepository.delete(squadCurrentMembers)
        return squadCurrentMembers
    }

    fun delete(squadCurrentMembers: List<SquadCurrentMembers>): List<SquadCurrentMembers> {
        squadCurrentMembersRepository.deleteAll(squadCurrentMembers)
        return squadCurrentMembers
    }

    fun delete(vararg squadCurrentMembers: SquadCurrentMembers): List<SquadCurrentMembers> {
        return delete(squadCurrentMembers.toMutableList())
    }
}
