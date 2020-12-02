package app.choppa.integration.domain.iteration

import app.choppa.domain.iteration.Iteration
import app.choppa.domain.iteration.IterationService
import app.choppa.exception.EntityNotFoundException
import app.choppa.support.factory.IterationFactory
import app.choppa.support.flyway.FlywayMigrationConfig
import app.choppa.support.matchers.containsInAnyOrder
import app.choppa.support.testcontainers.TestDBContainer
import com.natpryce.hamkrest.assertion.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
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
internal class IterationServiceCollectionIT @Autowired constructor(
    private val iterationService: IterationService,
) {
    @Container
    private val testDBContainer: TestDBContainer = TestDBContainer.get()

    private lateinit var entity: Iteration

    @Test
    @Transactional
    fun `Given a new list of iterations, when service saves said list of iterations, then service should persist the list of iterations`() {
        val newListOfIterations = IterationFactory.create(amount = 3)
        val result = iterationService.save(newListOfIterations)

        assertThat(result, List<Iteration>::containsInAnyOrder, newListOfIterations)
    }

    @Test
    @Transactional
    fun `Given an existing list of iterations, when service updates said list of iterations, then service should persist the changed list of iterations`() {
        val existingListOfIterations = iterationService.save(IterationFactory.create(amount = 3))
        val newNumber = (1..10).random()

        val updatedListOfIterations = existingListOfIterations.map {
            Iteration(it.id, newNumber)
        }

        val result = iterationService.save(updatedListOfIterations)

        assertThat(result, List<Iteration>::containsInAnyOrder, updatedListOfIterations)
    }

    @Test
    @Transactional
    fun `Given an existing list of iterations, when service deletes said list of iterations, then service should remove the existing list of iterations`() {
        val existingListOfIterations = iterationService.save(IterationFactory.create(amount = 3))
        val removedListOfIterations = iterationService.delete(existingListOfIterations)

        assertThrows(EntityNotFoundException::class.java) { iterationService.find(removedListOfIterations.map { it.id }) }
    }
}
