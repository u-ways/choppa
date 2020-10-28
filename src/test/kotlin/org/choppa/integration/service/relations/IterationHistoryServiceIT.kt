package org.choppa.integration.service.relations

import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeEmpty
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeNull
import org.choppa.model.Chapter
import org.choppa.model.Iteration
import org.choppa.model.Member
import org.choppa.model.Squad
import org.choppa.model.Tribe
import org.choppa.model.relations.IterationHistory
import org.choppa.model.relations.IterationHistoryId
import org.choppa.repository.relations.IterationHistoryRepository
import org.choppa.service.ChapterService
import org.choppa.service.IterationService
import org.choppa.service.MemberService
import org.choppa.service.SquadService
import org.choppa.service.TribeService
import org.choppa.service.relations.IterationHistoryService
import org.choppa.support.flyway.FlywayMigrationConfig
import org.choppa.support.testcontainers.TestDBContainer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.util.UUID.randomUUID
import javax.transaction.Transactional

private val CHAPTER = Chapter(id = randomUUID(), name = "chapterName")
private val MEMBER = Member(id = randomUUID(), name = "memberName", chapter = CHAPTER)
private val SQUAD = Squad(id = randomUUID(), name = "squadName")
private val TRIBE = Tribe(id = randomUUID(), name = "tribeName")
private val ITERATION = Iteration(id = randomUUID(), number = 100)

@SpringBootTest
@Testcontainers
@Import(FlywayMigrationConfig::class)
@ActiveProfiles("test")
internal class IterationHistoryServiceIT @Autowired constructor(
    private val iterationHistoryRepository: IterationHistoryRepository,
    private val chapterService: ChapterService,
    private val memberService: MemberService,
    private val squadService: SquadService,
    private val tribeService: TribeService,
    private val iterationService: IterationService,
    private val iterationHistoryService: IterationHistoryService
) {
    @Container
    private val testDBContainer: TestDBContainer = TestDBContainer.get()

    @BeforeEach
    @Transactional
    internal fun setUp() {
        memberService.save(MEMBER)
        squadService.save(SQUAD)
        tribeService.save(TRIBE)
        iterationService.save(ITERATION)
    }

    @Test
    fun `Given new entity, when service saves new entity, then service should return same entities with generated id`() {
        val entity = IterationHistory(ITERATION, TRIBE, SQUAD, MEMBER)
        val result = iterationHistoryService.save(entity)

        result.tribe.id shouldBeEqualTo entity.tribe.id
        result.squad.id shouldBeEqualTo entity.squad.id
        result.member.id shouldBeEqualTo entity.member.id

        result.tribe.name shouldBeEqualTo entity.tribe.name
        result.tribe.squads.shouldBeEmpty()
        entity.tribe.squads.shouldBeEmpty()

        result.squad.name shouldBeEqualTo entity.squad.name
        result.squad.members.shouldBeEmpty()
        entity.squad.members.shouldBeEmpty()
        result.squad.tribe shouldBeEqualTo entity.squad.tribe

        result.member.name shouldBeEqualTo entity.member.name
        result.member.chapter.id shouldBeEqualTo entity.member.chapter.id
        result.member.chapter.name shouldBeEqualTo entity.member.chapter.name
        result.member.squads.shouldBeEmpty()
        entity.member.squads.shouldBeEmpty()
    }

    @Test
    @Transactional
    fun `Given existing entity in db, when service finds entity by id, then service should return correct entities`() {
        val existingEntity = iterationHistoryService.save(IterationHistory(ITERATION, TRIBE, SQUAD, MEMBER))
        val result = iterationHistoryService.find(
            IterationHistoryId(
                existingEntity.iteration.id,
                existingEntity.tribe.id,
                existingEntity.squad.id,
                existingEntity.member.id
            )
        )

        result?.iteration shouldBe existingEntity.iteration
        result?.tribe shouldBe existingEntity.tribe
        result?.squad shouldBe existingEntity.squad
        result?.member shouldBe existingEntity.member
    }

    @Test
    @Transactional
    fun `Given existing entity in db, when service deletes entity, then service should removes entity from db`() {
        val existingEntity = iterationHistoryService.save(IterationHistory(ITERATION, TRIBE, SQUAD, MEMBER))
        val removedEntity = iterationHistoryService.delete(existingEntity)

        val result = iterationHistoryService.find(
            IterationHistoryId(
                removedEntity.iteration.id,
                removedEntity.tribe.id,
                removedEntity.squad.id,
                removedEntity.member.id
            )
        )

        result?.shouldBeNull()
    }

    @AfterEach
    internal fun tearDown() {
        iterationHistoryRepository.deleteAll()
        iterationService.delete(ITERATION)
        squadService.delete(SQUAD)
        tribeService.delete(TRIBE)
        memberService.delete(MEMBER)
        chapterService.delete(CHAPTER)
    }
}
