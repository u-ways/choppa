package app.choppa.integration.domain.squad

import app.choppa.domain.member.Member
import app.choppa.domain.squad.Squad
import app.choppa.domain.squad.SquadService
import app.choppa.domain.squad.history.RevisionType.ADD
import app.choppa.domain.squad.history.RevisionType.REMOVE
import app.choppa.domain.squad.history.SquadMemberHistoryService
import app.choppa.support.flyway.FlywayMigrationConfig
import app.choppa.support.testcontainers.TestDBContainer
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
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
internal class SquadServiceGeneratedSquadMemberRevisionsIT @Autowired constructor(
    private val squadService: SquadService,
    private val squadMemberHistoryService: SquadMemberHistoryService,
) {
    @Container
    private val testDBContainer: TestDBContainer = TestDBContainer.get()

    @Test
    @Transactional
    fun `Given existing squad no member formation, when service saves existing squad with revised formation, then a new squad member revision should be generated`() {
        val existingSquad = squadService.save(Squad())

        val existingSquadWithRevisedFormation = squadService.save(
            existingSquad.copy(members = mutableListOf(Member()))
        )

        assertDoesNotThrow {
            val result = squadMemberHistoryService.findBySquad(existingSquadWithRevisedFormation)

            result.size shouldBeEqualTo 1

            result.first().squad shouldBeEqualTo existingSquadWithRevisedFormation
            result.first().member shouldBeEqualTo existingSquadWithRevisedFormation.members[0]
            result.first().revisionNumber shouldBeEqualTo 0
            result.first().revisionType shouldBeEqualTo ADD
        }
    }

    @Test
    @Transactional
    fun `Given existing squad with existing member formation, when service saves existing squad with revised formation, then correct squad member revision should be generated`() {
        val existingSquad = squadService.save(
            Squad(members = mutableListOf(Member()))
        )

        val existingSquadWithRevisedFormation = squadService.save(
            existingSquad.copy(members = existingSquad.members.plus(Member()).toMutableList())
        )

        val result = squadMemberHistoryService.findBySquad(existingSquadWithRevisedFormation)

        result.first().squad shouldBeEqualTo existingSquadWithRevisedFormation
        result.first().member shouldBeEqualTo existingSquadWithRevisedFormation.members[1]
        result.first().revisionNumber shouldBeEqualTo 1
        result.first().revisionType shouldBeEqualTo ADD
    }

    @Test
    @Transactional
    fun `Given existing squad with existing member formation, when service saves existing squad with no formation, then correct squad member revision should be generated`() {
        val relatedMember = Member()

        val existingSquad = squadService.save(
            Squad(members = mutableListOf(relatedMember))
        )

        val existingSquadWithRevisedFormation = squadService.save(
            existingSquad.copy(members = mutableListOf())
        )

        val result = squadMemberHistoryService.findBySquad(existingSquadWithRevisedFormation)

        result.first().squad shouldBeEqualTo existingSquad
        result.first().member shouldBeEqualTo relatedMember
        result.first().revisionNumber shouldBeEqualTo 1
        result.first().revisionType shouldBeEqualTo REMOVE
    }
}
