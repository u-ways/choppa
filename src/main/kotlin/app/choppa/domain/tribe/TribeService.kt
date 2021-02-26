package app.choppa.domain.tribe

import app.choppa.domain.account.Account
import app.choppa.domain.base.BaseService
import app.choppa.domain.squad.SquadService
import app.choppa.exception.EntityNotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.io.Serializable
import java.util.*
import kotlin.collections.HashMap

@Service
class TribeService(
    @Autowired private val tribeRepository: TribeRepository,
    @Autowired private val squadService: SquadService,
) : BaseService<Tribe> {
    override fun find(account: Account): List<Tribe> = tribeRepository
        .findAll()
        .ownedBy(account)
        .orElseThrow { throw EntityNotFoundException("No tribes exist for this account yet.") }

    override fun find(ids: List<UUID>, account: Account): List<Tribe> = tribeRepository
        .findAllById(ids)
        .ownedBy(account)
        .orElseThrow { throw EntityNotFoundException("No tribes found with given ids.") }

    override fun find(id: UUID, account: Account): Tribe = tribeRepository
        .findById(id)
        .orElseThrow { throw EntityNotFoundException("Tribe with id [$id] does not exist.") }
        .verifyOwnership(account)

    override fun save(entity: Tribe, account: Account): Tribe = tribeRepository.save(
        tribeRepository
            .findById(entity.id)
            .verifyOriginalOwnership(entity, account)
    )

    override fun save(entities: List<Tribe>, account: Account): List<Tribe> = entities
        .map { this.save(it, account) }

    @Transactional
    override fun delete(entity: Tribe, account: Account): Tribe = tribeRepository.findById(entity.id).run {
        this.orElseGet { throw EntityNotFoundException("Tribe with id [${entity.id}] does not exist.") }
            .verifyOwnership(account).also {
                squadService.deleteRelatedByTribe(entity.id, account)
                tribeRepository.delete(entity)
            }
    }

    @Transactional
    override fun delete(entities: List<Tribe>, account: Account): List<Tribe> = entities
        .map { this.delete(it, account) }

    fun statistics(account: Account): Map<String, Serializable> =
        tribeRepository.findAll().ownedBy(account).run {
            mapOf(
                "total" to this.size,
                "knowledgeSharingPoints" to this.fold(HashMap<String, HashMap<String, HashMap<String, Any>>>(this.size)) { tribeMap, tribe ->
                    tribeMap.also {
                        tribeMap[tribe.id.toString()] =
                            tribe.squads.fold(HashMap<String, HashMap<String, Any>>(this.size)) { squadMap, squad ->
                                squadMap.also {
                                    squadMap[squad.id.toString()] =
                                        squadService.calculateKspForLastNRevisionsFor(squad.id, amount = 7)
                                }
                            }
                    }
                }
            )
        }

    private fun Optional<Tribe>.verifyOriginalOwnership(entity: Tribe, account: Account): Tribe =
        if (this.isPresent) entity.copy(account = this.get().account).verifyOwnership(account)
        else entity.copy(account = account)
}
