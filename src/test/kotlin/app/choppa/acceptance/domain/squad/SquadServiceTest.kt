package app.choppa.acceptance.domain.squad

import app.choppa.domain.member.MemberService
import app.choppa.domain.squad.Squad
import app.choppa.domain.squad.SquadRepository
import app.choppa.domain.squad.SquadService
import app.choppa.exception.EntityNotFoundException
import io.mockk.every
import io.mockk.mockkClass
import io.mockk.verify
import org.amshove.kluent.shouldBe
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.Optional.empty
import java.util.Optional.of
import java.util.UUID.randomUUID

internal class SquadServiceTest {
    private lateinit var repository: SquadRepository
    private lateinit var memberService: MemberService
    private lateinit var service: SquadService

    @BeforeEach
    internal fun setUp() {
        repository = mockkClass(SquadRepository::class)
        memberService = mockkClass(MemberService::class)
        service = SquadService(repository, memberService)
    }

    @Test
    fun `Given new entity, when service saves new entity, then service should save in repository and return the same entity`() {
        val entity = Squad()
        val tribe = entity.tribe
        val members = entity.members

        every { memberService.save(entity.members) } returns members
        every { repository.save(Squad(entity.id, entity.name, entity.color, tribe, members)) } returns entity

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
