package app.choppa.integration.domain.tribe

import app.choppa.domain.squad.SquadService
import app.choppa.domain.tribe.Tribe
import app.choppa.domain.tribe.TribeService
import app.choppa.exception.EntityNotFoundException
import app.choppa.support.base.BaseServiceIT
import app.choppa.support.factory.SquadFactory
import app.choppa.support.factory.TribeFactory
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import javax.transaction.Transactional

internal class TribeServiceIT @Autowired constructor(
    private val tribeService: TribeService,
    private val squadService: SquadService,
) : BaseServiceIT() {
    private lateinit var entity: Tribe

    @BeforeEach
    internal fun setUp() {
        entity = tribeService.save(TribeFactory.create())
    }

    @Test
    @Transactional
    fun `Given new entity, when service saves new entity, then service should return same entity with generated id`() {
        val result = tribeService.save(entity)

        result.id shouldBe entity.id
        result.name shouldBe entity.name
    }

    @Test
    @Transactional
    fun `Given existing entity in db, when service finds entity by id, then service should return correct entity`() {
        val existingEntity = entity
        val result = tribeService.find(existingEntity.id)

        result.id shouldBe existingEntity.id
        result.name shouldBe existingEntity.name
    }

    @Test
    @Transactional
    fun `Given existing entity in db, when service deletes entity, then service should removes entity from db`() {
        val existingEntity = tribeService.save(TribeFactory.create())
        val removedEntity = tribeService.delete(existingEntity)

        assertThrows(EntityNotFoundException::class.java) { tribeService.find(removedEntity.id) }
    }

    @Test
    @Transactional
    fun `Given existing entity in db with related records, when service deletes entity, then service should removes entity and related records from db`() {
        val existingEntity = tribeService.save(TribeFactory.create())
        val relatedSquad = squadService.save(SquadFactory.create(tribe = existingEntity))

        squadService.findRelatedByTribe(existingEntity.id).first() shouldBeEqualTo relatedSquad

        val removedEntity = tribeService.delete(existingEntity)

        assertThrows(EntityNotFoundException::class.java) { squadService.findRelatedByTribe(removedEntity.id) }
    }

    @AfterEach
    internal fun tearDown() {
        tribeService.delete(entity)
    }
}
