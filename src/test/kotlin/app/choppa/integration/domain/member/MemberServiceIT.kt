package app.choppa.integration.domain.member

import app.choppa.domain.chapter.Chapter
import app.choppa.domain.chapter.ChapterService
import app.choppa.domain.member.Member
import app.choppa.domain.member.MemberService
import app.choppa.domain.squad.SquadService
import app.choppa.domain.squad.history.SquadMemberHistoryService
import app.choppa.exception.EntityNotFoundException
import app.choppa.support.base.BaseServiceIT
import app.choppa.support.factory.ChapterFactory
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

internal class MemberServiceIT @Autowired constructor(
    private val chapterService: ChapterService,
    private val memberService: MemberService,
    private val squadService: SquadService,
    private val squadMemberHistoryService: SquadMemberHistoryService,
) : BaseServiceIT() {
    private lateinit var entity: Member
    private lateinit var relatedEntityChapter: Chapter

    @BeforeEach
    internal fun setUp() {
        relatedEntityChapter = chapterService.save(ChapterFactory.create())
        entity = memberService.save(MemberFactory.create(chapter = relatedEntityChapter))
    }

    @Test
    @Transactional
    fun `Given new entity, when service saves new entity, then service should return same entity with generated id`() {
        val result = memberService.save(entity)

        result.id shouldBe entity.id
        result.name shouldBe entity.name
        result.chapter.id shouldBe entity.chapter.id
        result.chapter.name shouldBe entity.chapter.name
    }

    @Test
    @Transactional
    fun `Given existing entity in db, when service finds entity by id, then service should return correct entity`() {
        val existingEntity = entity
        val result = memberService.find(existingEntity.id)

        result.id shouldBe existingEntity.id
        result.name shouldBe existingEntity.name
        result.chapter.id shouldBe existingEntity.chapter.id
        result.chapter.name shouldBe existingEntity.chapter.name
    }

    @Test
    @Transactional
    fun `Given existing entity in db, when service deletes entity, then service should removes entity from db`() {
        val existingEntity = memberService.save(MemberFactory.create())
        val removedEntity = memberService.delete(existingEntity)

        assertThrows(EntityNotFoundException::class.java) { memberService.find(removedEntity.id) }
    }

    @Test
    @Transactional
    fun `Given existing entity in db with related records, when service deletes entity, then service should removes entity and related records from db`() {
        val existingEntity = memberService.save(MemberFactory.create())
        val relatedSquad = squadService.save(SquadFactory.create(members = mutableListOf(existingEntity)))

        squadService.findRelatedByMember(existingEntity.id).first() shouldBeEqualTo relatedSquad
        assert(squadMemberHistoryService.findByMember(existingEntity).isNotEmpty())

        val removedEntity = memberService.delete(existingEntity)

        assertThrows(EntityNotFoundException::class.java) { squadService.findRelatedByMember(removedEntity.id) }
        assert(squadMemberHistoryService.findByMember(removedEntity).isEmpty())
    }

    @AfterEach
    internal fun tearDown() {
        memberService.delete(entity)
        chapterService.delete(relatedEntityChapter)
    }
}
