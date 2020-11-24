package org.choppa.integration.domain.chapter

import com.natpryce.hamkrest.assertion.assertThat
import org.choppa.domain.chapter.Chapter
import org.choppa.domain.chapter.ChapterService
import org.choppa.domain.member.Member
import org.choppa.domain.member.MemberService
import org.choppa.domain.squad.Squad
import org.choppa.domain.squad.SquadService
import org.choppa.domain.tribe.Tribe
import org.choppa.domain.tribe.TribeService
import org.choppa.support.factory.ChapterFactory
import org.choppa.support.flyway.FlywayMigrationConfig
import org.choppa.support.matchers.containsInAnyOrder
import org.choppa.support.testcontainers.TestDBContainer
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
