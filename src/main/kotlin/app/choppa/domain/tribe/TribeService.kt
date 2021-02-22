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
    override fun find(): List<Tribe> = tribeRepository
        .findAll()
        .orElseThrow { throw EntityNotFoundException("No tribes exist yet.") }

    fun find(account: Account): List<Tribe> = tribeRepository
        .findAll()
        .filter { it.account == account }
        .orElseThrow { throw EntityNotFoundException("No tribes exist for this account yet.") }

    override fun find(ids: List<UUID>): List<Tribe> = tribeRepository
        .findAllById(ids)
        .orElseThrow { throw EntityNotFoundException("No tribes found with given ids.") }

    override fun find(id: UUID): Tribe = tribeRepository
        .findById(id)
        .orElseThrow { throw EntityNotFoundException("Tribe with id [$id] does not exist.") }

    override fun save(entity: Tribe): Tribe = tribeRepository
        .save(entity)

    override fun save(entities: List<Tribe>): List<Tribe> = tribeRepository
        .saveAll(entities)

    @Transactional
    override fun delete(entity: Tribe): Tribe = entity.apply {
        squadService.deleteRelatedByTribe(entity.id)
        tribeRepository.delete(entity)
    }

    @Transactional
    override fun delete(entities: List<Tribe>): List<Tribe> = entities.map(::delete)

    fun statistics(): Map<String, Serializable> = tribeRepository.findAll().run {
        mapOf(
            "Total" to this.size,
            "Knowledge Sharing Points" to this.fold(HashMap<String, HashMap<String, HashMap<String, Any>>>(this.size)) { tribeMap, tribe ->
                tribeMap.also {
                    tribeMap[tribe.name] = tribe.squads.fold(HashMap<String, HashMap<String, Any>>(this.size)) { squadMap, squad ->
                        squadMap.also {
                            squadMap[squad.name] = squadService.calculateKspForLastNRevisionsFor(squad.id, amount = 7)
                        }
                    }
                }
            }
        )
    }
}
