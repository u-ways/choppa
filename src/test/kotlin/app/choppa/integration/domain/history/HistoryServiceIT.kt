package app.choppa.integration.domain.history

import app.choppa.domain.chapter.Chapter
import app.choppa.domain.chapter.ChapterService
import app.choppa.domain.history.History
import app.choppa.domain.history.HistoryService
import app.choppa.domain.iteration.Iteration
import app.choppa.domain.iteration.IterationService
import app.choppa.domain.member.Member
import app.choppa.domain.member.MemberService
import app.choppa.domain.squad.Squad
import app.choppa.domain.squad.SquadService
import app.choppa.domain.tribe.Tribe
import app.choppa.domain.tribe.TribeService
import app.choppa.support.flyway.FlywayMigrationConfig
import app.choppa.support.testcontainers.TestDBContainer
import org.amshove.kluent.shouldBeEmpty
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.AfterEach
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
internal class HistoryServiceIT @Autowired constructor(
    private val chapterService: ChapterService,
    private val memberService: MemberService,
    private val squadService: SquadService,
    private val tribeService: TribeService,
    private val iterationService: IterationService,
    private val historyService: HistoryService
) {
    @Container
    private val testDBContainer: TestDBContainer = TestDBContainer.get()

    private lateinit var chapter: Chapter
    private lateinit var member: Member
    private lateinit var squad: Squad
    private lateinit var tribe: Tribe
    private lateinit var iteration: Iteration

    private lateinit var entity: History

    @BeforeEach
    @Transactional
    internal fun setUp() {
        chapter = chapterService.save(Chapter())
        member = memberService.save(Member())
        squad = squadService.save(Squad())
        tribe = tribeService.save(Tribe())
        iteration = iterationService.save(Iteration())

        entity = historyService.save(History(iteration, tribe, squad, member, chapter))
    }

    @Test
    @Transactional
    fun `Given new entity, when service saves new entity, then service should return same entities with generated id`() {
        val result = historyService.save(entity)

        result.tribe!!.id shouldBeEqualTo entity.tribe!!.id
        result.squad!!.id shouldBeEqualTo entity.squad!!.id
        result.member!!.id shouldBeEqualTo entity.member!!.id

        result.tribe!!.name shouldBeEqualTo entity.tribe!!.name
        result.tribe!!.squads.shouldBeEmpty()
        entity.tribe!!.squads.shouldBeEmpty()

        result.squad!!.name shouldBeEqualTo entity.squad!!.name
        result.squad!!.members.shouldBeEmpty()
        entity.squad!!.members.shouldBeEmpty()
        result.squad!!.tribe shouldBeEqualTo entity.squad!!.tribe

        result.member!!.name shouldBeEqualTo entity.member!!.name
        result.member!!.chapter.id shouldBeEqualTo entity.member!!.chapter.id
        result.member!!.chapter.name shouldBeEqualTo entity.member!!.chapter.name
    }

    @AfterEach
    internal fun tearDown() {
        iterationService.delete(iteration)
        squadService.delete(squad)
        tribeService.delete(tribe)
        memberService.delete(member)

        historyService.delete(entity)
    }
}
