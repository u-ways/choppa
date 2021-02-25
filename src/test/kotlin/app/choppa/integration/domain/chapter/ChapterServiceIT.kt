package app.choppa.integration.domain.chapter

import app.choppa.domain.account.Account.Companion.UNASSIGNED_ACCOUNT
import app.choppa.domain.chapter.Chapter
import app.choppa.domain.chapter.ChapterService
import app.choppa.domain.member.Member
import app.choppa.domain.member.MemberService
import app.choppa.exception.EntityNotFoundException
import app.choppa.support.flyway.FlywayMigrationConfig
import app.choppa.support.testcontainers.TestDBContainer
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
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
internal class ChapterServiceIT @Autowired constructor(
    private val chapterService: ChapterService,
    private val memberService: MemberService,
) {
    @Container
    private val testDBContainer: TestDBContainer = TestDBContainer.get()

    private lateinit var entity: Chapter

    @BeforeEach
    internal fun setUp() {
        entity = chapterService.save(Chapter(), UNASSIGNED_ACCOUNT)
    }

    @Test
    fun `Given new entity, when service saves new entity, then service should return same entity with generated id`() {
        val result = chapterService.save(entity, UNASSIGNED_ACCOUNT)

        result.id shouldBe entity.id
        result.name shouldBe entity.name
    }

    @Test
    @Transactional
    fun `Given existing entity in db, when service finds entity by id, then service should return correct entity`() {
        val existingEntity = entity
        val result = chapterService.find(existingEntity.id, UNASSIGNED_ACCOUNT)

        result.id shouldBe existingEntity.id
        result.name shouldBe existingEntity.name
    }

    @Test
    @Transactional
    fun `Given existing entity in db, when service deletes entity, then service should removes entity from db`() {
        val existingEntity = chapterService.save(Chapter(), UNASSIGNED_ACCOUNT)
        val removedEntity = chapterService.delete(existingEntity, UNASSIGNED_ACCOUNT)

        assertThrows(EntityNotFoundException::class.java) { chapterService.find(removedEntity.id, UNASSIGNED_ACCOUNT) }
    }

    @Test
    @Transactional
    fun `Given existing entity in db with related records, when service deletes entity, then service should removes entity and related records from db`() {
        val existingEntity = chapterService.save(Chapter(), UNASSIGNED_ACCOUNT)
        val relatedMember = memberService.save(Member(chapter = existingEntity), UNASSIGNED_ACCOUNT)

        memberService.findRelatedByChapter(existingEntity.id, UNASSIGNED_ACCOUNT).first() shouldBeEqualTo relatedMember

        val removedEntity = chapterService.delete(existingEntity, UNASSIGNED_ACCOUNT)

        assertThrows(EntityNotFoundException::class.java) { memberService.findRelatedByChapter(removedEntity.id, UNASSIGNED_ACCOUNT) }
    }

    @AfterEach
    internal fun tearDown() {
        chapterService.delete(entity, UNASSIGNED_ACCOUNT)
    }
}
