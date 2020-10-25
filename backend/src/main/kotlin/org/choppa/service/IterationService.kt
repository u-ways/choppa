package org.choppa.service

import org.choppa.model.Iteration
import org.choppa.repository.IterationRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class IterationService(
    @Autowired private val iterationRepository: IterationRepository
) {
    fun find(id: UUID): Iteration? {
        return iterationRepository.findById(id).orElseGet { null }
    }

    fun find(): List<Iteration> {
        return iterationRepository.findAll()
    }

    fun find(ids: List<UUID>): List<Iteration> {
        return iterationRepository.findAllById(ids)
    }

    fun save(iteration: Iteration): Iteration {
        return iterationRepository.save(iteration)
    }

    fun save(iterations: List<Iteration>): List<Iteration> {
        return iterationRepository.saveAll(iterations)
    }

    fun save(vararg iteration: Iteration): List<Iteration> {
        return save(iteration.toMutableList())
    }

    fun delete(iteration: Iteration): Iteration {
        iterationRepository.delete(iteration)
        return iteration
    }

    fun delete(iterations: List<Iteration>): List<Iteration> {
        iterationRepository.deleteAll(iterations)
        return iterations
    }

    fun delete(vararg iterations: Iteration): List<Iteration> {
        return delete(iterations.toMutableList())
    }
}
