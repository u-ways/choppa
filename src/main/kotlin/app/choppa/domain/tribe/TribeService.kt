package app.choppa.domain.tribe

import app.choppa.domain.account.AccountService
import app.choppa.domain.base.BaseService
import app.choppa.domain.rotation.smr.calculateMemberPairingPoints
import app.choppa.domain.rotation.smr.calculateSquadPairingPoints
import app.choppa.domain.squad.SquadService
import app.choppa.exception.EntityNotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.io.Serializable
import java.util.*
import kotlin.collections.HashMap

@Service
class TribeService(
    @Autowired private val tribeRepository: TribeRepository,
    @Autowired private val squadService: SquadService,
    @Autowired private val accountService: AccountService,
) : BaseService<Tribe>(accountService) {

    override fun find(sort: Sort): List<Tribe> = tribeRepository
        .findAll()
        .ownedByAuthenticated()
        .orElseThrow { throw EntityNotFoundException("No tribes exist for this account yet.") }

    override fun find(ids: List<UUID>, sort: Sort): List<Tribe> = tribeRepository
        .findAllById(ids)
        .ownedByAuthenticated()
        .orElseThrow { throw EntityNotFoundException("No tribes found with given ids.") }

    override fun find(id: UUID, sort: Sort): Tribe = tribeRepository
        .findById(id)
        .orElseThrow { throw EntityNotFoundException("Tribe with id [$id] does not exist.") }
        .verifyAuthenticatedOwnership()

    override fun save(entity: Tribe): Tribe = tribeRepository.save(
        tribeRepository
            .findById(entity.id)
            .verifyOriginalOwnership(entity)
    )

    override fun save(entities: List<Tribe>): List<Tribe> = entities
        .map { this.save(it) }

    @Transactional
    override fun delete(entity: Tribe): Tribe = tribeRepository.findById(entity.id).run {
        this.orElseGet { throw EntityNotFoundException("Tribe with id [${entity.id}] does not exist.") }
            .verifyAuthenticatedOwnership().also {
                squadService.deleteRelatedByTribe(entity.id)
                tribeRepository.delete(entity)
            }
    }

    @Transactional
    override fun delete(entities: List<Tribe>): List<Tribe> = entities
        .map { this.delete(it) }

    fun statistics(): Map<String, Serializable> =
        tribeRepository.findAll().ownedByAuthenticated().run {
            mapOf(
                "total" to this.size,
                "knowledgeSharingPoints" to this.fold(HashMap<String, HashMap<String, HashMap<String, Any>>>(this.size)) { tribeMap, tribe ->
                    val histories = squadService.findSquadsRevisionsAndMemberDurations(
                        squadService.findRelatedByTribe(tribe.id)
                    )
                    tribeMap.also {
                        val squadMap: HashMap<String, HashMap<String, Any>> = hashMapOf()
                        tribe.squads.forEach { squadMap[it.id.toString()] = hashMapOf() }
                        for (i in (1..4)) {
                            val activeHistories = histories.map { Pair(it.first, it.second.drop(i)) }
                            val mpp = calculateMemberPairingPoints(activeHistories.map { it.second }.flatten())
                            val spp = calculateSquadPairingPoints(activeHistories)

                            tribe.squads.forEach {
                                squadMap[it.id.toString()]!![i.toString()] = hashMapOf("ksp" to squadService.calculateKspForLastNRevisionsFor(it.id, mpp, spp).times(1000))
                            }
                        }

                        tribeMap[tribe.id.toString()] = squadMap
                    }
                }
            )
        }

    private fun Optional<Tribe>.verifyOriginalOwnership(entity: Tribe): Tribe =
        if (this.isPresent) entity.copy(account = this.get().account).verifyAuthenticatedOwnership()
        else entity.copy(account = accountService.resolveFromAuth())
}
