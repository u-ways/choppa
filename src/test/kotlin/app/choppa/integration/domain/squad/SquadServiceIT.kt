package app.choppa.integration.domain.squad

import app.choppa.domain.member.MemberService
import app.choppa.domain.squad.Squad
import app.choppa.domain.squad.SquadService
import app.choppa.domain.squad.history.SquadMemberHistoryService
import app.choppa.exception.EntityNotFoundException
import app.choppa.support.base.BaseServiceIT
import app.choppa.support.factory.MemberFactory
import app.choppa.support.factory.SquadFactory
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import javax.transaction.Transactional

internal class SquadServiceIT @Autowired constructor(
    private val squadService: SquadService,
    private val memberService: MemberService,
    private val squadMemberHistoryService: SquadMemberHistoryService,
) : BaseServiceIT() {
    private lateinit var entity: Squad

    @BeforeEach
    internal fun setUp() {
        entity = squadService.save(SquadFactory.create())
    }

    @Test
    @Transactional
    fun `Given new entity, when service saves new entity, then service should return same entity with generated id`() {
        val result = squadService.save(entity)

        result.id shouldBe entity.id
        result.name shouldBe entity.name
    }

    @Test
    @Transactional
    fun `Given existing entity in db, when service finds entity by id, then service should return correct entity`() {
        val existingEntity = entity
        val result = squadService.find(existingEntity.id)

        result.id shouldBe existingEntity.id
        result.name shouldBe existingEntity.name
    }

    @Test
    @Transactional
    fun `Given existing entity in db with existing member that doesn't belong to any other squad, when service updates entity by removing member, then service should remove form squad and set member to inactive`() {
        val relatedMember = memberService.save(MemberFactory.create(active = true))
        val existingEntity = squadService.save(SquadFactory.create(members = mutableListOf(relatedMember)))

        val updatedEntity = squadService.save(existingEntity.copy(members = existingEntity.members.minus(relatedMember).toMutableList()))
        val removedMember = memberService.find(relatedMember.id)

        updatedEntity.members.isEmpty().shouldBe(true)
        removedMember.active.shouldBe(false)
    }

    @Test
    @Transactional
    fun `Given existing entity in db, when service deletes entity, then service should removes entity from db`() {
        val existingEntity = squadService.save(SquadFactory.create())
        val removedEntity = squadService.delete(existingEntity)

        assertThrows(EntityNotFoundException::class.java) { squadService.find(removedEntity.id) }
    }

    @Test
    @Transactional
    fun `Given existing entity in db with related records, when service deletes entity, then service should removes entity and related records from db`() {
        val relatedMember = memberService.save(MemberFactory.create())
        val existingEntity = squadService.save(SquadFactory.create(members = mutableListOf(relatedMember)))

        memberService.findRelatedBySquad(existingEntity.id).first() shouldBeEqualTo relatedMember
        assert(squadMemberHistoryService.findBySquad(existingEntity).isNotEmpty())

        val removedEntity = squadService.delete(existingEntity)

        assertThrows(EntityNotFoundException::class.java) { memberService.findRelatedBySquad(removedEntity.id) }
        assert(squadMemberHistoryService.findBySquad(removedEntity).isEmpty())
    }

    @AfterEach
    internal fun tearDown() {
        squadService.delete(entity)
    }
}
