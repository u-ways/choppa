package app.choppa.integration.domain.history

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
import app.choppa.support.factory.HistoryFactory
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
import java.time.Instant.EPOCH
import java.time.Instant.now
import javax.transaction.Transactional

@SpringBootTest
@Testcontainers
@Import(FlywayMigrationConfig::class)
@ActiveProfiles("test")
internal class HistoryServiceRelatedEntitiesIT @Autowired constructor(
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
    fun `Given current iteration with history records, when service find history by current iteration, then service should return related history records`() {
        val currentIteration = iterationService.save(Iteration())
        val relatedTribe = tribeService.save(
            Tribe(squads = mutableListOf(squadService.save(Squad(members = mutableListOf(memberService.save(Member()))))))
        )
        val expectedHistoryRecords = historyService.save(HistoryFactory.create(relatedTribe, currentIteration))
        val actualHistoryRecords = historyService.findRelatedByIteration(currentIteration.id)

        assertThat(actualHistoryRecords, List<History>::containsInAnyOrder, expectedHistoryRecords)
    }

    @Test
    @Transactional
    fun `Given tribe with history records, when service find history by related tribe, then service should return related history records`() {
        val currentIteration = iterationService.save(Iteration())
        val relatedTribe = tribeService.save(
            Tribe(squads = mutableListOf(squadService.save(Squad(members = mutableListOf(memberService.save(Member()))))))
        )

        val expectedHistoryRecords = historyService.save(HistoryFactory.create(relatedTribe, currentIteration))
        val actualHistoryRecords = historyService.findRelatedByTribe(relatedTribe.id)

        assertThat(actualHistoryRecords, List<History>::containsInAnyOrder, expectedHistoryRecords)
    }

    @Test
    @Transactional
    fun `Given squad with history records, when service find history by related squad, then service should return related history records`() {
        val currentIteration = iterationService.save(Iteration())
        val relatedTribe = tribeService.save(
            Tribe(squads = mutableListOf(squadService.save(Squad(members = mutableListOf(memberService.save(Member()))))))
        )
        val relatedSquad = relatedTribe.squads.first()

        val expectedHistoryRecords = historyService.save(HistoryFactory.create(relatedTribe, currentIteration))
        val actualHistoryRecords = historyService.findRelatedBySquad(relatedSquad.id)

        assertThat(actualHistoryRecords, List<History>::containsInAnyOrder, expectedHistoryRecords)
    }

    @Test
    @Transactional
    fun `Given member with history records, when service find history by related member, then service should return related history records`() {
        val currentIteration = iterationService.save(Iteration())
        val relatedTribe = tribeService.save(
            Tribe(squads = mutableListOf(squadService.save(Squad(members = mutableListOf(memberService.save(Member()))))))
        )
        val relatedMember = relatedTribe.squads.first().members.first()

        val expectedHistoryRecords = historyService.save(HistoryFactory.create(relatedTribe, currentIteration))
        val actualHistoryRecords = historyService.findRelatedByMember(relatedMember.id)

        assertThat(actualHistoryRecords, List<History>::containsInAnyOrder, expectedHistoryRecords)
    }

    @Test
    @Transactional
    fun `Given history records with createDate, when service find history before createDate, then service should return related history records`() {
        val currentIteration = iterationService.save(Iteration())
        val relatedTribe = tribeService.save(
            Tribe(squads = mutableListOf(squadService.save(Squad(members = mutableListOf(memberService.save(Member()))))))
        )

        val createDate = EPOCH
        val expectedHistoryRecords =
            historyService.save(HistoryFactory.create(relatedTribe, currentIteration, createDate))
        val actualHistoryRecords = historyService.findAllByCreateDateBefore(createDate.plusSeconds(1L))

        assertThat(actualHistoryRecords, List<History>::containsInAnyOrder, expectedHistoryRecords)
    }

    @Test
    @Transactional
    fun `Given history records with createDate, when service find history after createDate, then service should return related history records`() {
        val currentIteration = iterationService.save(Iteration())
        val relatedTribe = tribeService.save(
            Tribe(squads = mutableListOf(squadService.save(Squad(members = mutableListOf(memberService.save(Member()))))))
        )

        val createDate = now()
        val expectedHistoryRecords =
            historyService.save(HistoryFactory.create(relatedTribe, currentIteration, createDate))
        val actualHistoryRecords = historyService.findAllByCreateDateAfter(createDate.minusSeconds(1L))

        assertThat(actualHistoryRecords, List<History>::containsInAnyOrder, expectedHistoryRecords)
    }
}
