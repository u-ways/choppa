package app.choppa.domain.rotation

import app.choppa.domain.rotation.RotationContext.Companion.rotate
import app.choppa.domain.squad.SquadService
import app.choppa.domain.tribe.Tribe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RotationService(
    @Autowired private val squadService: SquadService,
) {
    // TODO(u-ways) #175 Use Squad revisions to aid in SMR.
    //  For example, if you want a List [ Pair<Squad,Revisions> ] DS. Use the following:
    //    tribe.squads.map { squadService.findAllSquadMembersRevisions(it.id) }
    @Transactional
    fun executeRotation(tribe: Tribe, options: RotationOptions): Tribe = tribe.copy(
        squads = squadService.save(rotate(tribe, options).squads).toMutableList()
    )

    @Transactional
    fun undoRotation(tribe: Tribe): Tribe = tribe.copy(
        squads = squadService.save(
            tribe.squads
                .map { squadService.findLastNSquadMembersRevisions(it.id, 1) }
                .filter { it.first.members == it.second.first() }
                .map { it.first.copy(members = it.second.first().toMutableList()) }
                .toMutableList()
        ).toMutableList()
    )
}
