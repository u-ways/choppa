package org.choppa.integration.service

import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeNull
import org.choppa.model.Chapter
import org.choppa.model.Member
import org.choppa.repository.ChapterRepository
import org.choppa.repository.MemberRepository
import org.choppa.service.MemberService
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
import java.util.UUID
import javax.transaction.Transactional

private val CHAPTER = Chapter(id = UUID.randomUUID(), name = "chapterName")
private const val MEMBER_NAME = "memberName"

@SpringBootTest
@Testcontainers
@Import(FlywayMigrationConfig::class)
@ActiveProfiles("test")
internal class MemberServiceIT @Autowired constructor(
    private val chapterRepository: ChapterRepository,
    private val memberRepository: MemberRepository,
    private val memberService: MemberService
) {
    @Container
    private val testDBContainer: TestDBContainer = TestDBContainer.get()

    @Test
    @Transactional
    fun givenNewEntity_WhenServiceSavesNewEntity_ThenServiceShouldReturnSameEntityWithGeneratedId() {
        val entity = Member(name = MEMBER_NAME, chapter = CHAPTER)
        val result = memberService.save(entity)

        result.id shouldBe entity.id
        result.name shouldBe entity.name
        result.chapter.id shouldBe entity.chapter.id
        result.chapter.name shouldBe entity.chapter.name
    }

    @Test
    @Transactional
    fun givenExistingEntityInDb_WhenServiceFindsEntityById_ThenServiceShouldReturnCorrectEntity() {
        val existingEntity = memberService.save(Member(name = MEMBER_NAME, chapter = CHAPTER))
        val result = memberService.find(existingEntity.id)

        result?.id shouldBe existingEntity.id
        result?.name shouldBe existingEntity.name
        result?.chapter?.id shouldBe existingEntity.chapter.id
        result?.chapter?.name shouldBe existingEntity.chapter.name
    }

    @Test
    @Transactional
    fun givenExistingEntityInDb_WhenServiceDeletesEntity_ThenServiceShouldRemovesEntityFromDb() {
        val existingEntity = memberService.save(Member(name = MEMBER_NAME, chapter = CHAPTER))
        val removedEntity = memberService.delete(existingEntity)
        val result = memberService.find(removedEntity.id)

        result?.shouldBeNull()
    }

    @AfterEach
    internal fun tearDown() {
        memberRepository.deleteAll()
        chapterRepository.deleteAll()
    }
}
