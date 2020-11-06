package org.choppa.integration.service

import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeNull
import org.choppa.model.iteration.Iteration
import org.choppa.repository.IterationRepository
import org.choppa.service.IterationService
import org.choppa.support.flyway.FlywayMigrationConfig
import org.choppa.support.testcontainers.TestDBContainer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import javax.transaction.Transactional

private const val ITERATION_NUMBER = 100

@SpringBootTest
@Testcontainers
@Import(FlywayMigrationConfig::class)
@ActiveProfiles("test")
internal class IterationServiceIT @Autowired constructor(
    private val iterationRepository: IterationRepository,
    private val iterationService: IterationService,
) {
    @Container
    private val testDBContainer: TestDBContainer = TestDBContainer.get()

    @Test
    @Transactional
    fun `Given new entity, when service saves new entity, then service should return same entity with generated id`() {
        val entity = Iteration(number = ITERATION_NUMBER)
        val result = iterationService.save(entity)

        result.id shouldBe entity.id
        result.number shouldBe entity.number
        result.startDate shouldBe entity.startDate
        result.endDate shouldBe entity.endDate
    }

    @Test
    @Transactional
    fun `Given existing entity in db, when service finds entity by id, then service should return correct entity`() {
        val existingEntity = iterationService.save(Iteration(number = ITERATION_NUMBER))
        val result = iterationService.find(existingEntity.id)

        result?.id shouldBe existingEntity.id
        result?.number shouldBe existingEntity.number
        result?.startDate shouldBe existingEntity.startDate
        result?.endDate shouldBe existingEntity.endDate
    }

    @Test
    @Transactional
    fun `Given existing entity in db, when service deletes entity, then service should removes entity from db`() {
        val existingEntity = iterationService.save(Iteration(number = ITERATION_NUMBER))
        val removedEntity = iterationService.delete(existingEntity)
        val result = iterationService.find(removedEntity.id)

        result?.shouldBeNull()
    }

    @AfterEach
    internal fun tearDown() {
        iterationRepository.deleteAll()
    }
}
