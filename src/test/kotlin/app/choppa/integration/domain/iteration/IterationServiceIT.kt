package app.choppa.integration.domain.iteration

import app.choppa.domain.iteration.Iteration
import app.choppa.domain.iteration.IterationService
import app.choppa.exception.EntityNotFoundException
import app.choppa.support.flyway.FlywayMigrationConfig
import app.choppa.support.testcontainers.TestDBContainer
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import javax.transaction.Transactional

@SpringBootTest
@Testcontainers
@Import(FlywayMigrationConfig::class)
@ActiveProfiles("test")
internal class IterationServiceIT @Autowired constructor(
    private val iterationService: IterationService
) {
    @Container
    private val testDBContainer: TestDBContainer = TestDBContainer.get()

    private lateinit var entity: Iteration

    @BeforeEach
    internal fun setUp() {
        entity = iterationService.save(Iteration())
    }

    @Test
    @Transactional
    fun `Given new entity, when service saves new entity, then service should return same entity with generated id`() {
        val result = iterationService.save(entity)

        result.id shouldBe entity.id
        result.number shouldBeEqualTo entity.number
        result.startDate shouldBeEqualTo entity.startDate
        result.endDate shouldBeEqualTo entity.endDate
    }

    @Test
    @Transactional
    fun `Given existing entity in db, when service finds entity by id, then service should return correct entity`() {
        val existingEntity = entity
        val result = iterationService.find(existingEntity.id)

        result.id shouldBe existingEntity.id
        result.number shouldBeEqualTo existingEntity.number
        result.startDate shouldBeEqualTo existingEntity.startDate
        result.endDate shouldBeEqualTo existingEntity.endDate
    }

    @Test
    @Transactional
    fun `Given existing entity in db, when service deletes entity, then service should removes entity from db`() {
        val existingEntity = entity
        val removedEntity = iterationService.delete(existingEntity)

        assertThrows(EntityNotFoundException::class.java) { iterationService.find(removedEntity.id) }
    }

    @AfterEach
    internal fun tearDown() {
        iterationService.delete(entity)
    }
}
