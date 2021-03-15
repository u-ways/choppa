package app.choppa.integration.domain.chapter

import app.choppa.domain.chapter.Chapter
import app.choppa.domain.chapter.ChapterService
import app.choppa.domain.member.MemberService
import app.choppa.domain.squad.SquadService
import app.choppa.domain.tribe.TribeService
import app.choppa.support.base.BaseServiceIT
import app.choppa.support.factory.ChapterFactory
import app.choppa.support.factory.MemberFactory
import app.choppa.support.factory.SquadFactory
import app.choppa.support.factory.TribeFactory
import app.choppa.support.matchers.containsInAnyOrder
import com.natpryce.hamkrest.assertion.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import javax.transaction.Transactional

internal class ChapterServiceRelatedEntitiesIT @Autowired constructor(
    private val chapterService: ChapterService,
    private val memberService: MemberService,
    private val squadService: SquadService,
    private val tribeService: TribeService,
) : BaseServiceIT() {

    @Test
    @Transactional
    fun `Given tribe with chapters, when service find chapters by related tribe, then service should return related chapters`() {
        val relatedTribe = tribeService.save(TribeFactory.create())

        val chapters = chapterService.save(ChapterFactory.create(3))
        val expectedRelatedTribeChapters = chapters.minus(chapters.first())

        val tribeSquadMembers = memberService.save(
            listOf(
                MemberFactory.create(chapter = expectedRelatedTribeChapters[0]),
                MemberFactory.create(chapter = expectedRelatedTribeChapters[1])
            )
        ).toMutableList()

        squadService.save(SquadFactory.create(tribe = relatedTribe, members = tribeSquadMembers))

        val actualRelatedTribeChapters = chapterService.findRelatedByTribe(relatedTribe.id)

        assertThat(actualRelatedTribeChapters, List<Chapter>::containsInAnyOrder, expectedRelatedTribeChapters)
    }

    @Test
    @Transactional
    fun `Given squad with chapters, when service find chapters by related squad, then service should return related chapters`() {
        val chapters = chapterService.save(ChapterFactory.create(4))
        val expectedRelatedSquadChapters = chapters.minus(chapters.first())

        val tribeSquadMembers = memberService.save(
            listOf(
                MemberFactory.create(chapter = expectedRelatedSquadChapters[0]),
                MemberFactory.create(chapter = expectedRelatedSquadChapters[1]),
                MemberFactory.create(chapter = expectedRelatedSquadChapters[2])
            )
        ).toMutableList()

        val relatedSquad = squadService.save(SquadFactory.create(members = tribeSquadMembers))
        val actualRelatedSquadChapters = chapterService.findRelatedBySquad(relatedSquad.id)

        assertThat(actualRelatedSquadChapters, List<Chapter>::containsInAnyOrder, expectedRelatedSquadChapters)
    }
}
