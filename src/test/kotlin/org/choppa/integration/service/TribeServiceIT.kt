package org.choppa.integration.service

import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeNull
import org.choppa.model.Tribe
import org.choppa.repository.TribeRepository
import org.choppa.service.TribeService
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

private const val TRIBE_NAME = "tribeName"

@SpringBootTest
@Testcontainers
@Import(FlywayMigrationConfig::class)
@ActiveProfiles("test")
internal class TribeServiceIT @Autowired constructor(
    private val tribeRepository: TribeRepository,
    private val tribeService: TribeService,
) {
    @Container
    private val testDBContainer: TestDBContainer = TestDBContainer.get()

    @Test
    @Transactional
    fun givenNewEntity_WhenServiceSavesNewEntity_ThenServiceShouldReturnSameEntityWithGeneratedId() {
        val entity = Tribe(name = TRIBE_NAME)
        val result = tribeService.save(entity)

        result.id shouldBe entity.id
        result.name shouldBe entity.name
    }

    @Test
    @Transactional
    fun givenExistingEntityInDb_WhenServiceFindsEntityById_ThenServiceShouldReturnCorrectEntity() {
        val existingEntity = tribeService.save(Tribe(name = TRIBE_NAME))
        val result = tribeService.find(existingEntity.id)

        result?.id shouldBe existingEntity.id
        result?.name shouldBe existingEntity.name
    }

    @Test
    @Transactional
    fun givenExistingEntityInDb_WhenServiceDeletesEntity_ThenServiceShouldRemovesEntityFromDb() {
        val existingEntity = tribeService.save(Tribe(name = TRIBE_NAME))
        val removedEntity = tribeService.delete(existingEntity)
        val result = tribeService.find(removedEntity.id)

        result?.shouldBeNull()
    }

    @AfterEach
    internal fun tearDown() {
        tribeRepository.deleteAll()
    }
}
