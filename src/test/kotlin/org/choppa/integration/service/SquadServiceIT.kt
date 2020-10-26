package org.choppa.integration.service

import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeNull
import org.choppa.model.Squad
import org.choppa.repository.SquadRepository
import org.choppa.service.SquadService
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

private const val SQUAD_NAME = "squadName"

@SpringBootTest
@Testcontainers
@Import(FlywayMigrationConfig::class)
@ActiveProfiles("test")
internal class SquadServiceIT @Autowired constructor(
    private val squadRepository: SquadRepository,
    private val squadService: SquadService,
) {
    @Container
    private val testDBContainer: TestDBContainer = TestDBContainer.get()

    @Test
    @Transactional
    fun `Given new entity, when service saves new entity, then service should return same entity with generated id`() {
        val entity = Squad(name = SQUAD_NAME)
        val result = squadService.save(entity)

        result.id shouldBe entity.id
        result.name shouldBe entity.name
    }

    @Test
    @Transactional
    fun `Given existing entity in db, when service finds entity by id, then service should return correct entity`() {
        val existingEntity = squadService.save(Squad(name = SQUAD_NAME))
        val result = squadService.find(existingEntity.id)

        result?.id shouldBe existingEntity.id
        result?.name shouldBe existingEntity.name
    }

    @Test
    @Transactional
    fun `Given existing entity in db, when service deletes entity, then service should removes entity from db`() {
        val existingEntity = squadService.save(Squad(name = SQUAD_NAME))
        val removedEntity = squadService.delete(existingEntity)
        val result = squadService.find(removedEntity.id)

        result?.shouldBeNull()
    }

    @AfterEach
    internal fun tearDown() {
        squadRepository.deleteAll()
    }
}
