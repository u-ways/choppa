package app.choppa.acceptance.domain.squad

import app.choppa.domain.member.Member
import app.choppa.domain.member.MemberService
import app.choppa.domain.squad.Squad
import app.choppa.domain.squad.SquadRepository
import app.choppa.domain.squad.SquadService
import app.choppa.domain.squad.history.SquadMemberHistoryService
import app.choppa.exception.EntityNotFoundException
import app.choppa.support.factory.SquadFactory
import app.choppa.support.matchers.containsInAnyOrder
import com.natpryce.hamkrest.assertion.assertThat
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
    private lateinit var squadMemberHistoryService: SquadMemberHistoryService
    private lateinit var service: SquadService

    @BeforeEach
    internal fun setUp() {
        repository = mockkClass(SquadRepository::class)
        memberService = mockkClass(MemberService::class)
        squadMemberHistoryService = mockkClass(SquadMemberHistoryService::class, relaxed = true)
        service = SquadService(repository, memberService, squadMemberHistoryService)
    }

    @Test
    fun `Given new entity, when service saves new entity, then service should save in repository and return the same entity`() {
        val entity = Squad()
        val tribe = entity.tribe
        val members = entity.members

        every { memberService.save(entity.members) } returns members

        every { repository.findById(entity.id) } returns empty()

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
        every { repository.deleteAllSquadMemberRecordsFor(existingEntity.id) } returns Unit

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

    @Test
    fun `Given existing entity, when service findAllSquadMembersRevisions for existing entity, then service should pair entity with related revisions`() {
        val existingEntity = SquadFactory.create(membersAmount = 5)

        every { repository.findById(existingEntity.id) } returns of(existingEntity)
        every { repository.deleteAllSquadMemberRecordsFor(existingEntity.id) } returns Unit
        every { squadMemberHistoryService.concentrateAllSquadRevisions(existingEntity) } returns listOf(existingEntity.members)

        val squadMembersRevisions = service.findAllSquadMembersRevisions(existingEntity.id)

        squadMembersRevisions.first shouldBe existingEntity
        squadMembersRevisions.second.first() shouldBe existingEntity.members
    }

    @Test
    fun `Given existing entity, when service findLastNSquadMembersRevisions for existing entity, then service should pair entity with correct revisions`() {
        val existingEntity = SquadFactory.create(membersAmount = 5)

        every { repository.findById(existingEntity.id) } returns of(existingEntity)

        every {
            squadMemberHistoryService.concentrateLastNSquadRevisions(existingEntity, 1)
        } returns existingEntity.members.subList(0, 4)

        every {
            squadMemberHistoryService.concentrateLastNSquadRevisions(existingEntity, 2)
        } returns existingEntity.members.subList(0, 3)

        val squadMembersRevisions = service.findLastNSquadMembersRevisions(existingEntity.id, 2)

        squadMembersRevisions.first shouldBe existingEntity

        assertThat(
            squadMembersRevisions.second.first(),
            List<Member>::containsInAnyOrder,
            existingEntity.members.subList(0, 4)
        )

        assertThat(
            squadMembersRevisions.second.last(),
            List<Member>::containsInAnyOrder,
            existingEntity.members.subList(0, 3)
        )
    }
}
