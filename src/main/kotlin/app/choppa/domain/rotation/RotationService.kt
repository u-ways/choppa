package app.choppa.domain.rotation

import app.choppa.domain.account.Account
import app.choppa.domain.rotation.RotationContext.Companion.rotate
import app.choppa.domain.squad.SquadService
import app.choppa.domain.tribe.Tribe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation.REPEATABLE_READ
import org.springframework.transaction.annotation.Transactional

@Service
class RotationService(
    @Autowired private val squadService: SquadService,
) {
    @Transactional(isolation = REPEATABLE_READ)
    fun executeRotation(tribe: Tribe, options: RotationOptions, account: Account): Tribe = rotate(
        tribe,
        options,
        tribe.squads.map { squadService.findAllSquadMembersRevisions(it.id, account) }
    ).apply { squadService.save(this.squads, account) }

    @Transactional(isolation = REPEATABLE_READ)
    fun undoRotation(tribe: Tribe, account: Account): Tribe = tribe.copy(
        squads = squadService.save(
            squadService.findRelatedByTribe(tribe.id, account).map {
                it.copy(
                    members = squadService.findLastNSquadMembersRevisions(it.id, 1, account)
                        .second
                        .first()
                        .toMutableList()
                )
            }.toMutableList(), account).toMutableList()
    )
}
