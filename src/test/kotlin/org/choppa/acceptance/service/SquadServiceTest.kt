package org.choppa.acceptance.service

import io.mockk.every
import io.mockk.mockkClass
import io.mockk.verify
import org.amshove.kluent.shouldBe
import org.choppa.exception.EntityNotFoundException
import org.choppa.model.squad.Squad
import org.choppa.repository.SquadRepository
import org.choppa.service.MemberService
import org.choppa.service.SquadService
import org.choppa.service.TribeService
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.Optional.empty
import java.util.Optional.of
import java.util.UUID.randomUUID

internal class SquadServiceTest {
    private lateinit var repository: SquadRepository
    private lateinit var tribeService: TribeService
    private lateinit var memberService: MemberService
    private lateinit var service: SquadService

    @BeforeEach
    internal fun setUp() {
        repository = mockkClass(SquadRepository::class)
        tribeService = mockkClass(TribeService::class, relaxed = true)
        memberService = mockkClass(MemberService::class, relaxed = true)

        service = SquadService(repository, tribeService, memberService)
    }

    @Test
    fun `Given new entity, when service saves new entity, then service should save in repository and return the same entity`() {
        val entity = Squad()
        val tribe = entity.tribe
        val members = entity.members

        every { tribeService.find(entity.tribe.id) } returns tribe
        every { memberService.find(entity.members.map { it.id }) } returns members
        every { repository.save(Squad(entity.id, entity.name, tribe, members)) } returns entity

        val savedEntity = service.save(entity)

        savedEntity shouldBe entity

        verify(exactly = 1) { repository.save(entity) }
    }

    @Test
    fun `Given existing entity, when service looks for existing entity by id, then service should find using repository and return existing entity`() {
        val id = randomUUID()
        val existingEntity = Squad(id)

        every { repository.findById(id) } returns of(existingEntity)

        val foundEntity = service.find(id)

        foundEntity shouldBe existingEntity

        verify(exactly = 1) { repository.findById(id) }
    }

    @Test
    fun `Given existing entity, when service deletes existing entity, then service should delete using repository`() {
        val existingEntity = Squad()

        every { repository.delete(existingEntity) } returns Unit
        every { repository.findById(existingEntity.id) } returns empty()

        val removedEntity = service.delete(existingEntity)

        removedEntity shouldBe existingEntity

        verify(exactly = 1) { repository.delete(existingEntity) }
    }

    @Test
    fun `Given a non-existent entity UUID, when service tries to find by said UUID, then service should throw EntityNotFoundException`() {
        val id = randomUUID()

        every { repository.findById(id) } returns empty()

        assertThrows(EntityNotFoundException::class.java) { service.find(id) }
    }
}
