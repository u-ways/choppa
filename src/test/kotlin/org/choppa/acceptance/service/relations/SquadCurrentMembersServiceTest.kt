package org.choppa.acceptance.service.relations

import io.mockk.every
import io.mockk.mockkClass
import io.mockk.verify
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeNull
import org.choppa.model.Chapter
import org.choppa.model.Member
import org.choppa.model.Squad
import org.choppa.model.relations.SquadCurrentMembers
import org.choppa.model.relations.SquadCurrentMembersId
import org.choppa.repository.relations.SquadCurrentMembersRepository
import org.choppa.service.relations.SquadCurrentMembersService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.Optional.empty
import java.util.Optional.of
import java.util.UUID.randomUUID

private val CHAPTER = Chapter(id = randomUUID(), name = "chapterName")
private val MEMBER = Member(id = randomUUID(), name = "memberName", chapter = CHAPTER)
private val SQUAD = Squad(id = randomUUID(), name = "squadName")

internal class SquadCurrentMembersServiceTest {
    private lateinit var repository: SquadCurrentMembersRepository
    private lateinit var service: SquadCurrentMembersService

    @BeforeEach
    internal fun setUp() {
        repository = mockkClass(SquadCurrentMembersRepository::class)
        service = SquadCurrentMembersService(repository)
    }

    @Test
    fun `Given new entity when, service saves new entity, then service should save in repository and return the same entity`() {
        val entity = SquadCurrentMembers(SQUAD, MEMBER)

        every { repository.save(entity) } returns entity

        val savedEntity = service.save(entity)

        savedEntity shouldBe entity

        verify(exactly = 1) { repository.save(entity) }
    }

    @Test
    fun `Given existing entity, when service looks for existing entity by id, then service should find using repository and return existing entity`() {
        val id = SquadCurrentMembersId(SQUAD.id, MEMBER.id)
        val existingEntity = SquadCurrentMembers(SQUAD, MEMBER)

        every { repository.findById(id) } returns of(existingEntity)

        val foundEntity = service.find(id)

        foundEntity shouldBe existingEntity

        verify(exactly = 1) { repository.findById(id) }
    }

    @Test
    fun `Given existing entity when, service deletes existing entity, then service should delete using repository`() {
        val id = SquadCurrentMembersId(SQUAD.id, MEMBER.id)
        val existingEntity = SquadCurrentMembers(SQUAD, MEMBER)

        every { repository.delete(existingEntity) } returns Unit
        every { repository.findById(id) } returns empty()

        val removedEntity = service.delete(existingEntity)

        removedEntity shouldBe existingEntity

        val nonExistentEntity = service.find(id)

        nonExistentEntity.shouldBeNull()

        verify(exactly = 1) { repository.delete(existingEntity) }
    }
}
