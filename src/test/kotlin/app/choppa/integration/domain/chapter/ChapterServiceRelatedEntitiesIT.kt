package app.choppa.integration.domain.chapter

import app.choppa.domain.chapter.Chapter
import app.choppa.domain.chapter.ChapterService
import app.choppa.domain.member.Member
import app.choppa.domain.member.MemberService
import app.choppa.domain.squad.Squad
import app.choppa.domain.squad.SquadService
import app.choppa.domain.tribe.Tribe
import app.choppa.domain.tribe.TribeService
import app.choppa.support.factory.ChapterFactory
import app.choppa.support.flyway.FlywayMigrationConfig
import app.choppa.support.matchers.containsInAnyOrder
import app.choppa.support.testcontainers.TestDBContainer
import com.natpryce.hamkrest.assertion.assertThat
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
internal class ChapterServiceRelatedEntitiesIT @Autowired constructor(
    private val chapterService: ChapterService,
    private val memberService: MemberService,
    private val squadService: SquadService,
    private val tribeService: TribeService,
) {

    @Container
    private val testDBContainer: TestDBContainer = TestDBContainer.get()

    @Test
    @Transactional
    fun `Given tribe with chapters, when service find chapters by related tribe, then service should return related chapters`() {
        val relatedTribe = tribeService.save(Tribe())

        val chapters = chapterService.save(ChapterFactory.create(3))
        val expectedRelatedTribeChapters = chapters.minus(chapters.first())

        val tribeSquadMembers = memberService.save(
            Member(chapter = expectedRelatedTribeChapters[0]),
            Member(chapter = expectedRelatedTribeChapters[1])
        ).toMutableList()

        squadService.save(Squad(tribe = relatedTribe, members = tribeSquadMembers))

        val actualRelatedTribeChapters = chapterService.findRelatedByTribe(relatedTribe.id)

        assertThat(actualRelatedTribeChapters, List<Chapter>::containsInAnyOrder, expectedRelatedTribeChapters)
    }

    @Test
    @Transactional
    fun `Given squad with chapters, when service find chapters by related squad, then service should return related chapters`() {
        val chapters = chapterService.save(ChapterFactory.create(4))
        val expectedRelatedSquadChapters = chapters.minus(chapters.first())

        val tribeSquadMembers = memberService.save(
            Member(chapter = expectedRelatedSquadChapters[0]),
            Member(chapter = expectedRelatedSquadChapters[1]),
            Member(chapter = expectedRelatedSquadChapters[2])
        ).toMutableList()

        val relatedSquad = squadService.save(Squad(members = tribeSquadMembers))

        val actualRelatedSquadChapters = chapterService.findRelatedBySquad(relatedSquad.id)

        assertThat(actualRelatedSquadChapters, List<Chapter>::containsInAnyOrder, expectedRelatedSquadChapters)
    }
}
