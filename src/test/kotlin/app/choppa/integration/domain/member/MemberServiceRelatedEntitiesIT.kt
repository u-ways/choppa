package app.choppa.integration.domain.member

import app.choppa.domain.chapter.Chapter
import app.choppa.domain.chapter.ChapterService
import app.choppa.domain.member.Member
import app.choppa.domain.member.MemberService
import app.choppa.domain.squad.Squad
import app.choppa.domain.squad.SquadService
import app.choppa.domain.tribe.Tribe
import app.choppa.domain.tribe.TribeService
import app.choppa.support.factory.MemberFactory
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
internal class MemberServiceRelatedEntitiesIT @Autowired constructor(
    private val chapterService: ChapterService,
    private val memberService: MemberService,
    private val squadService: SquadService,
    private val tribeService: TribeService,
) {

    @Container
    private val testDBContainer: TestDBContainer = TestDBContainer.get()

    @Test
    @Transactional
    fun `Given tribe with members, when service find members by related tribe, then service should return related members`() {
        memberService.save(MemberFactory.create(2)) // random members

        val tribeMembersOfSquadA = memberService.save(MemberFactory.create(3))
        val tribeMembersOfSquadB = memberService.save(MemberFactory.create(3))

        val relatedTribe = tribeService.save(Tribe())
        val expectedRelatedTribeMembers = tribeMembersOfSquadA.plus(tribeMembersOfSquadB)

        squadService.save(
            listOf(
                Squad(tribe = relatedTribe, members = tribeMembersOfSquadA.toMutableList()),
                Squad(tribe = relatedTribe, members = tribeMembersOfSquadB.toMutableList())
            )
        )

        val actualRelatedTribeMembers = memberService.findRelatedByTribe(relatedTribe.id)

        assertThat(actualRelatedTribeMembers, List<Member>::containsInAnyOrder, expectedRelatedTribeMembers)
    }

    @Test
    @Transactional
    fun `Given squad with members, when service find members by related squad, then service should return related members`() {
        memberService.save(MemberFactory.create(2)) // random members

        val expectedRelatedSquadMembers = memberService.save(MemberFactory.create(3))

        val relatedSquad = squadService.save(Squad(members = expectedRelatedSquadMembers.toMutableList()))

        val actualRelatedSquadMembers = memberService.findRelatedBySquad(relatedSquad.id)

        assertThat(actualRelatedSquadMembers, List<Member>::containsInAnyOrder, expectedRelatedSquadMembers)
    }

    @Test
    @Transactional
    fun `Given member with members, when service find members by related chapter, then service should return related members`() {
        memberService.save(MemberFactory.create(2)) // random members

        val relatedChapter = chapterService.save(Chapter())
        val expectedRelatedChapterMembers = memberService.save(MemberFactory.create(5, relatedChapter))

        val actualRelatedChapterMembers = memberService.findRelatedByChapter(relatedChapter.id)

        assertThat(actualRelatedChapterMembers, List<Member>::containsInAnyOrder, expectedRelatedChapterMembers)
    }
}
