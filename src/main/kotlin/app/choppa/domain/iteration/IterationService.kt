package app.choppa.domain.iteration

import app.choppa.domain.account.Account
import app.choppa.domain.base.BaseService
import app.choppa.exception.EntityNotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class IterationService(
    @Autowired private val iterationRepository: IterationRepository
) : BaseService<Iteration> {
    override fun find(account: Account): List<Iteration> = iterationRepository
        .findAll()
        .ownedBy(account)
        .orElseThrow { throw EntityNotFoundException("No iterations exist yet.") }

    override fun find(id: UUID, account: Account): Iteration = iterationRepository
        .findById(id)
        .orElseThrow { throw EntityNotFoundException("Iteration with id [$id] does not exist.") }
        .verifyOwnership(account)

    override fun find(ids: List<UUID>, account: Account): List<Iteration> = iterationRepository
        .findAllById(ids)
        .ownedBy(account)
        .orElseThrow { throw EntityNotFoundException("No iterations found with given ids.") }

    override fun save(entity: Iteration, account: Account): Iteration = iterationRepository.save(
        iterationRepository
            .findById(entity.id)
            .verifyOriginalOwnership(entity, account)
    )

    override fun save(entities: List<Iteration>, account: Account): List<Iteration> = entities
        .map { this.save(it, account) }

    override fun delete(entity: Iteration, account: Account): Iteration = iterationRepository.findById(entity.id).run {
        this.orElseGet { throw EntityNotFoundException("Iteration with id [${entity.id}] does not exist.") }
            .verifyOwnership(account).also {
                iterationRepository.delete(entity)
            }
    }

    override fun delete(entities: List<Iteration>, account: Account): List<Iteration> = entities
        .map { this.delete(it, account) }

    private fun Optional<Iteration>.verifyOriginalOwnership(entity: Iteration, account: Account): Iteration =
        if (this.isPresent) entity.copy(account = this.get().account).verifyOwnership(account)
        else entity.copy(account = account)
}
