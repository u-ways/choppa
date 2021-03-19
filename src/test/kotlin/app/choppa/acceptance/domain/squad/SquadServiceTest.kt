package app.choppa.acceptance.domain.squad

import app.choppa.domain.account.AccountService
import app.choppa.domain.member.Member
import app.choppa.domain.member.MemberService
import app.choppa.domain.squad.Squad
import app.choppa.domain.squad.SquadRepository
import app.choppa.domain.squad.SquadService
import app.choppa.domain.squad.history.SquadMemberHistoryService
import app.choppa.exception.EntityNotFoundException
import app.choppa.support.base.Universe
import app.choppa.support.factory.MemberFactory
import app.choppa.support.factory.SquadFactory
import app.choppa.support.matchers.containsInAnyOrder
import com.natpryce.hamkrest.assertion.assertThat
import io.mockk.every
import io.mockk.mockkClass
import io.mockk.verify
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.Optional.empty
import java.util.Optional.of
import java.util.UUID.randomUUID

internal class SquadServiceTest : Universe() {
    private lateinit var repository: SquadRepository
    private lateinit var memberService: MemberService
    private lateinit var squadMemberHistoryService: SquadMemberHistoryService
    private lateinit var accountService: AccountService
    private lateinit var service: SquadService

    @BeforeEach
    internal fun setUp() {
        repository = mockkClass(SquadRepository::class)
        memberService = mockkClass(MemberService::class)
        accountService = mockkClass(AccountService::class, relaxed = true)
        squadMemberHistoryService = mockkClass(SquadMemberHistoryService::class, relaxed = true)
        service = SquadService(repository, memberService, squadMemberHistoryService, accountService)

        every { accountService.resolveFromAuth() } returns ACCOUNT
    }

    @Test
    fun `Given new entity, when service saves new entity, then service should save in repository and return the same entity`() {
        val entity = SquadFactory.create()
        val tribe = entity.tribe
        val members = entity.members

        every { memberService.save(entity.members) } returns members

        every { repository.findById(entity.id) } returns empty()

        every {
            repository.save(
                Squad(
                    entity.id,
                    entity.name,
                    entity.color,
                    tribe,
                    members,
                    account = ACCOUNT
                )
            )
        } returns entity

        val savedEntity = service.save(entity)

        savedEntity shouldBeEqualTo entity

        verify(exactly = 1) { repository.save(entity) }
    }

    @Test
    fun `Given existing entity, when service looks for existing entity by id, then service should find using repository and return existing entity`() {
        val id = randomUUID()
        val existingEntity = SquadFactory.create(id)

        every { repository.findById(id) } returns of(existingEntity)

        val foundEntity = service.find(id)

        foundEntity shouldBeEqualTo existingEntity

        verify(exactly = 1) { repository.findById(id) }
    }

    @Test
    fun `Given existing entity, when service deletes existing entity, then service should delete using repository`() {
        val existingEntity = SquadFactory.create()

        every { repository.findById(existingEntity.id) } returns of(existingEntity)
        every { repository.delete(existingEntity) } returns Unit
        every { repository.deleteAllSquadMemberRecordsFor(existingEntity.id) } returns Unit

        val removedEntity = service.delete(existingEntity)

        removedEntity shouldBeEqualTo existingEntity

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
        val existingEntity = SquadFactory.create().apply {
            members.addAll(MemberFactory.create(5))
        }

        every { repository.findById(existingEntity.id) } returns of(existingEntity)
        every { repository.deleteAllSquadMemberRecordsFor(existingEntity.id) } returns Unit
        every { squadMemberHistoryService.concentrateAllSquadRevisions(existingEntity) } returns listOf(existingEntity.members)

        val squadMembersRevisions = service.findAllSquadMembersRevisions(existingEntity.id)

        squadMembersRevisions.first shouldBeEqualTo existingEntity
        squadMembersRevisions.second.first() shouldBeEqualTo existingEntity.members
    }

    @Test
    fun `Given existing entity, when service findLastNSquadMembersRevisions for existing entity, then service should pair entity with correct revisions`() {
        val existingEntity = SquadFactory.create().apply {
            members.addAll(MemberFactory.create(5))
        }

        every { repository.findById(existingEntity.id) } returns of(existingEntity)

        every {
            squadMemberHistoryService.concentrateLastNSquadRevisions(existingEntity, 1)
        } returns existingEntity.members.subList(0, 4)

        every {
            squadMemberHistoryService.concentrateLastNSquadRevisions(existingEntity, 2)
        } returns existingEntity.members.subList(0, 3)

        val squadMembersRevisions = service.findLastNSquadMembersRevisions(existingEntity.id, 2)

        squadMembersRevisions.first shouldBeEqualTo existingEntity

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
