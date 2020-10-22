package choppaorg.choppa.service

import choppaorg.choppa.exception.EntityNotFoundException
import choppaorg.choppa.model.Member
import choppaorg.choppa.model.Squad
import choppaorg.choppa.model.SquadMember
import choppaorg.choppa.repository.SquadMemberRepository
import choppaorg.choppa.repository.SquadRepository
import org.springframework.stereotype.Service
import java.time.Clock
import java.time.LocalDateTime

@Service
class SquadService(
    private val squadRepository: SquadRepository,
    private val squadMemberRepository: SquadMemberRepository,
    private val clock: Clock
) {

    fun create(name: String): Squad {
        return squadRepository.save(Squad(name));
    }

    fun findById(id: Int): Squad {
        return squadRepository
            .findById(id)
            .orElseThrow { EntityNotFoundException("No squad found with id $id") }
    }

    fun addMember(squad: Squad, member: Member) {
        val memberAlreadyAdded = squad.members
            .map { sm -> sm.member.id }
            .contains(member.id)

        if (memberAlreadyAdded) {
            return
        }

        val squadMember = SquadMember(squad, member, LocalDateTime.now(clock))
        squadMemberRepository.save(squadMember)
        squad.members = setOf(*squad.members.toTypedArray(), squadMember)
    }
}