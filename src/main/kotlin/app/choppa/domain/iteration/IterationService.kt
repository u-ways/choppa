package app.choppa.domain.iteration

import app.choppa.domain.base.BaseService
import app.choppa.exception.EntityNotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class IterationService(
    @Autowired private val iterationRepository: IterationRepository
) : BaseService<Iteration> {
    override fun find(id: UUID): Iteration = iterationRepository
        .findById(id)
        .orElseThrow { throw EntityNotFoundException("Iteration with id [$id] does not exist.") }

    override fun save(entity: Iteration): Iteration = iterationRepository
        .save(entity)

    override fun delete(entity: Iteration): Iteration = entity
        .apply { iterationRepository.delete(entity) }

    fun find(): List<Iteration> = iterationRepository
        .findAll()
        .orElseThrow { throw EntityNotFoundException("No iterations exist yet.") }

    fun find(ids: List<UUID>): List<Iteration> = iterationRepository
        .findAllById(ids)
        .orElseThrow { throw EntityNotFoundException("No iterations found with given ids.") }

    fun save(iterations: List<Iteration>): List<Iteration> = iterationRepository
        .saveAll(iterations)

    fun save(vararg iteration: Iteration): List<Iteration> = this
        .save(iteration.toMutableList())

    fun delete(iterations: List<Iteration>): List<Iteration> = iterations
        .apply { iterationRepository.deleteAll(iterations) }

    fun delete(vararg iterations: Iteration): List<Iteration> = this
        .delete(iterations.toMutableList())
}
