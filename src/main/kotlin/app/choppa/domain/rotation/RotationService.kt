package app.choppa.domain.rotation

import app.choppa.core.rotation.Rotation
import app.choppa.core.rotation.RotationOptions
import app.choppa.domain.history.History
import app.choppa.domain.history.HistoryService
import app.choppa.domain.squad.SquadService
import app.choppa.domain.tribe.Tribe
import app.choppa.domain.tribe.TribeService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class RotationService(
    @Autowired private val tribeService: TribeService,
    @Autowired private val squadService: SquadService,
    @Autowired private val historyService: HistoryService,
) {
    @Transactional
    fun rotate(tribeToRotate: Tribe, options: RotationOptions): Tribe {
        val rotatedTribe = updateTribeHistory(tribeToRotate, Rotation.rotate(tribeToRotate, options))
        val newHistories = rotatedTribe.history.filter { !tribeToRotate.history.contains(it) }
        if (newHistories.count() > 0) {
            val squadServiceRotations = newHistories.map { it.squad }.distinct()
            squadService.save(squadServiceRotations)
            historyService.save(newHistories)
        }
        tribeService.save(rotatedTribe)
        return rotatedTribe
    }

    // MD485: (TODO) Replace this when Uways's history saving is in. This is why it is untested.
    private fun updateTribeHistory(originalTribe: Tribe, rotatedTribe: Tribe): Tribe = rotatedTribe.apply {
        this.history.addAll(
            rotatedTribe.squads.let { squadList ->
                val latestIteration =
                    if (originalTribe.history.count() == 0)
                        throw Exception("Tribes should always have a history.")
                    else
                        originalTribe.history[0].iteration

                squadList.mapIndexed { index, squad ->
                    squad.members.filter { member ->
                        !originalTribe.squads[index].members.contains(member)
                    }.map {
                        History(latestIteration, rotatedTribe, squad, it)
                    }
                }.flatten()
            }
        )
    }
}
