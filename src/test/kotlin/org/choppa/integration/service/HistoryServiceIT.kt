package org.choppa.integration.service

import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeEmpty
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeNull
import org.choppa.model.chapter.Chapter
import org.choppa.model.history.History
import org.choppa.model.history.HistoryId
import org.choppa.model.iteration.Iteration
import org.choppa.model.member.Member
import org.choppa.model.squad.Squad
import org.choppa.model.tribe.Tribe
import org.choppa.repository.HistoryRepository
import org.choppa.service.ChapterService
import org.choppa.service.HistoryService
import org.choppa.service.IterationService
import org.choppa.service.MemberService
import org.choppa.service.SquadService
import org.choppa.service.TribeService
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
internal class HistoryServiceIT @Autowired constructor(
    private val historyRepository: HistoryRepository,
    private val chapterService: ChapterService,
    private val memberService: MemberService,
    private val squadService: SquadService,
    private val tribeService: TribeService,
    private val iterationService: IterationService,
    private val historyService: HistoryService
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
        val entity = History(ITERATION, TRIBE, SQUAD, MEMBER)
        val result = historyService.save(entity)

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
        val existingEntity = historyService.save(History(ITERATION, TRIBE, SQUAD, MEMBER))
        val result = historyService.find(
            HistoryId(
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
        val existingEntity = historyService.save(History(ITERATION, TRIBE, SQUAD, MEMBER))
        val removedEntity = historyService.delete(existingEntity)

        val result = historyService.find(
            HistoryId(
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
        historyRepository.deleteAll()
        iterationService.delete(ITERATION)
        squadService.delete(SQUAD)
        tribeService.delete(TRIBE)
        memberService.delete(MEMBER)
        chapterService.delete(CHAPTER)
    }
}
