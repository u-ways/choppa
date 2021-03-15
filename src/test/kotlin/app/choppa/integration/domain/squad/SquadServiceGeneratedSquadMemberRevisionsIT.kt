package app.choppa.integration.domain.squad

import app.choppa.domain.squad.SquadService
import app.choppa.domain.squad.history.RevisionType.ADD
import app.choppa.domain.squad.history.RevisionType.REMOVE
import app.choppa.domain.squad.history.SquadMemberHistoryService
import app.choppa.support.base.BaseServiceIT
import app.choppa.support.factory.MemberFactory
import app.choppa.support.factory.SquadFactory
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.springframework.beans.factory.annotation.Autowired
import javax.transaction.Transactional

internal class SquadServiceGeneratedSquadMemberRevisionsIT @Autowired constructor(
    private val squadService: SquadService,
    private val squadMemberHistoryService: SquadMemberHistoryService,
) : BaseServiceIT() {

    @Test
    @Transactional
    fun `Given existing squad no member formation, when service saves existing squad with revised formation, then a new squad member revision should be generated`() {
        val existingSquad = squadService.save(SquadFactory.create())

        val existingSquadWithRevisedFormation = squadService.save(
            existingSquad.copy(members = mutableListOf(MemberFactory.create()))
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
            SquadFactory.create(members = mutableListOf(MemberFactory.create()))
        )

        val existingSquadWithRevisedFormation = squadService.save(
            existingSquad.copy(members = existingSquad.members.plus(MemberFactory.create()).toMutableList())
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
        val relatedMember = MemberFactory.create()

        val existingSquad = squadService.save(
            SquadFactory.create(members = mutableListOf(relatedMember))
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
