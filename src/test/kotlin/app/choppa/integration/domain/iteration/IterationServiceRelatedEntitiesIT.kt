package app.choppa.integration.domain.iteration

import app.choppa.domain.chapter.Chapter.Companion.UNASSIGNED_ROLE
import app.choppa.domain.history.History
import app.choppa.domain.history.HistoryService
import app.choppa.domain.iteration.Iteration
import app.choppa.domain.iteration.IterationService
import app.choppa.domain.member.Member
import app.choppa.domain.member.MemberService
import app.choppa.domain.squad.Squad
import app.choppa.domain.squad.Squad.Companion.UNASSIGNED_SQUAD
import app.choppa.domain.squad.SquadService
import app.choppa.domain.tribe.Tribe
import app.choppa.domain.tribe.Tribe.Companion.UNASSIGNED_TRIBE
import app.choppa.domain.tribe.TribeService
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
internal class IterationServiceRelatedEntitiesIT @Autowired constructor(
    private val historyService: HistoryService,
    private val memberService: MemberService,
    private val squadService: SquadService,
    private val tribeService: TribeService,
    private val iterationService: IterationService
) {

    @Container
    private val testDBContainer: TestDBContainer = TestDBContainer.get()

    @Test
    @Transactional
    fun `Given member with iteration records, when service find iteration by member, then service should return related iteration records`() {
        val currentIteration = iterationService.save(Iteration())
        val relatedMember = Member()

        historyService.save(
            History(
                currentIteration,
                UNASSIGNED_TRIBE,
                UNASSIGNED_SQUAD,
                memberService.save(relatedMember),
                relatedMember.chapter
            )
        )

        val expectedIterationRecords = mutableListOf(currentIteration)
        val actualIterationRecords = iterationService.findRelatedByMember(relatedMember.id)

        assertThat(actualIterationRecords, List<Iteration>::containsInAnyOrder, expectedIterationRecords)
    }

    @Test
    @Transactional
    fun `Given squad with iteration records, when service find iteration by related squad, then service should return related iteration records`() {
        val currentIteration = iterationService.save(Iteration())
        val relatedSquad = Squad()

        historyService.save(
            History(
                currentIteration,
                UNASSIGNED_TRIBE,
                squadService.save(relatedSquad),
                memberService.save(Member()),
                UNASSIGNED_ROLE
            )
        )

        val expectedIterationRecords = mutableListOf(currentIteration)
        val actualIterationRecords = iterationService.findRelatedBySquad(relatedSquad.id)

        assertThat(actualIterationRecords, List<Iteration>::containsInAnyOrder, expectedIterationRecords)
    }

    @Test
    @Transactional
    fun `Given tribe with iteration records, when service find iteration by related tribe, then service should return related iteration records`() {
        val currentIteration = iterationService.save(Iteration())
        val relatedTribe = Tribe()

        historyService.save(
            History(
                currentIteration,
                tribeService.save(relatedTribe),
                UNASSIGNED_SQUAD,
                memberService.save(Member()),
                UNASSIGNED_ROLE
            )
        )

        val expectedIterationRecords = mutableListOf(currentIteration)
        val actualIterationRecords = iterationService.findRelatedByTribe(relatedTribe.id)

        assertThat(actualIterationRecords, List<Iteration>::containsInAnyOrder, expectedIterationRecords)
    }
}
