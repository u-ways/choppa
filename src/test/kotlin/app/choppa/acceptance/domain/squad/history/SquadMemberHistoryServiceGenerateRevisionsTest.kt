package app.choppa.acceptance.domain.squad.history

import app.choppa.domain.account.AccountService
import app.choppa.domain.member.Member
import app.choppa.domain.member.MemberRepository
import app.choppa.domain.squad.Squad
import app.choppa.domain.squad.history.RevisionType.ADD
import app.choppa.domain.squad.history.RevisionType.REMOVE
import app.choppa.domain.squad.history.SquadMemberHistory
import app.choppa.domain.squad.history.SquadMemberHistoryRepository
import app.choppa.domain.squad.history.SquadMemberHistoryService
import app.choppa.support.factory.SquadMemberHistoryFactory
import io.mockk.every
import io.mockk.mockkClass
import org.amshove.kluent.shouldBeEqualTo
import org.hibernate.type.IntegerType.ZERO
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.data.domain.PageRequest.of
import org.springframework.data.domain.Sort.Direction.DESC
import org.springframework.data.domain.Sort.by
import java.util.stream.Stream

internal class SquadMemberHistoryServiceGenerateRevisionsTest {
    private lateinit var repository: SquadMemberHistoryRepository
    private lateinit var memberRepository: MemberRepository
    private lateinit var accountService: AccountService
    private lateinit var service: SquadMemberHistoryService

    @BeforeEach
    internal fun setUp() {
        repository = mockkClass(SquadMemberHistoryRepository::class)
        memberRepository = mockkClass(MemberRepository::class)
        accountService = mockkClass(AccountService::class)
        service = SquadMemberHistoryService(repository, memberRepository, accountService)
    }

    @Test
    fun `Given existing squad with no revised formations before, when service generate next squad revision for newly revised formations, then service should generate first revision of number zero`() {
        val existingSquad = Squad()
        val revisedFormation = listOf(Member())

        every {
            repository.findAllBySquad(
                existingSquad,
                of(ZERO, 1, by(DESC, SquadMemberHistory::revisionNumber.name))
            )
        } returns emptyList()

        val actualRevisionsGenerated = service.generateRevisions(
            existingSquad.apply { this.members.addAll(revisedFormation) },
            emptyList()
        )

        actualRevisionsGenerated.size shouldBeEqualTo 1
        actualRevisionsGenerated[0].revisionNumber shouldBeEqualTo ZERO
    }

    /**
     *  R# = Revision number #
     *  S# = Squad    number #
     *  M# = Member   number #
     *
     * | Test # | Existing Formation | Revised Formation | Next Revision # | Expected Revisions Generated          |
     * |:-------|:-------------------|:------------------|:----------------|:--------------------------------------|
     * |  1     | {M1}               | {}                | 1               | [{S0,M1,R1,REMOVE}]                   |
     * |  2     | {}                 | {M1}              | 2               | [{S0,M1,R2,ADD}]                      |
     * |  3     | {M1}               | {M1,M2}           | 3               | [{S0,M2,R3,ADD}]                      |
     * |  4     | {M1,M2}            | {M1,M2,M3,M4}     | 4               | [{S0,M3,R4,ADD},{S0,M4,R4,ADD}]       |
     * |  5     | {M1,M2,M3,M4}      | {M1,M2}           | 5               | [{S0,M3,R5,REMOVE},{S0,M4,R5,REMOVE}] |
     * |  6     | {M1,M2}            | {M1,M3}           | 6               | [{S0,M3,R6,ADD},{S0,M2,R6,REMOVE}]    |
     */
    @ParameterizedTest
    @MethodSource("generateRevisionsTestArgs")
    fun `Given existing squad with a revised formation, when service generate next squad revision, then service should generate correct records`(
        existingSquad: Squad,
        revisedFormation: List<Member>,
        nextRevisionNumber: Int,
        expectedRevisionsGenerated: List<SquadMemberHistory>,
    ) {
        every {
            repository.findAllBySquad(
                existingSquad,
                of(ZERO, 1, by(DESC, SquadMemberHistory::revisionNumber.name))
            )
        } returns listOf(SquadMemberHistoryFactory.create(revisionNumber = nextRevisionNumber - 1))

        val olderFormation = mutableListOf<Member>().apply { this.addAll(existingSquad.members) }

        val actualRevisionsGenerated = service.generateRevisions(
            existingSquad.apply {
                this.members.clear()
                this.members.addAll(revisedFormation)
            },
            olderFormation
        )

        actualRevisionsGenerated.size shouldBeEqualTo expectedRevisionsGenerated.size

        actualRevisionsGenerated.forEachIndexed { index, actualRevision: SquadMemberHistory ->
            actualRevision.squad shouldBeEqualTo expectedRevisionsGenerated[index].squad
            actualRevision.member shouldBeEqualTo expectedRevisionsGenerated[index].member
            actualRevision.revisionNumber shouldBeEqualTo expectedRevisionsGenerated[index].revisionNumber
            actualRevision.revisionType shouldBeEqualTo expectedRevisionsGenerated[index].revisionType
        }
    }

    companion object {
        @JvmStatic
        fun generateRevisionsTestArgs(): Stream<Arguments?>? {
            return Stream.of(
                Triple(
                    Squad(),
                    listOf<Member>(),
                    1
                ).let { (existingSquad, revisedFormation, nextRevisionNumber) ->
                    arguments(
                        existingSquad.apply { this.members.add(Member()) },
                        revisedFormation,
                        nextRevisionNumber,
                        listOf(
                            SquadMemberHistory(existingSquad, existingSquad.members[0], nextRevisionNumber, REMOVE)
                        )
                    )
                },

                Triple(
                    Squad(),
                    mutableListOf<Member>(),
                    2
                ).let { (existingSquad, revisedFormation, nextRevisionNumber) ->
                    arguments(
                        existingSquad,
                        revisedFormation.apply { this.add(Member()) },
                        nextRevisionNumber,
                        listOf(
                            SquadMemberHistory(existingSquad, revisedFormation[0], nextRevisionNumber, ADD)
                        )
                    )
                },

                Triple(
                    Squad(),
                    mutableListOf<Member>(),
                    3
                ).let { (existingSquad, revisedFormation, nextRevisionNumber) ->
                    arguments(
                        existingSquad.apply { this.members.add(Member()) },
                        revisedFormation.apply {
                            this.add(existingSquad.members[0])
                            this.add(Member())
                        },
                        nextRevisionNumber,
                        listOf(
                            SquadMemberHistory(existingSquad, revisedFormation[1], nextRevisionNumber, ADD)
                        )
                    )
                },

                Triple(
                    Squad(),
                    mutableListOf<Member>(),
                    4
                ).let { (existingSquad, revisedFormation, nextRevisionNumber) ->
                    arguments(
                        existingSquad.apply {
                            this.members.add(Member())
                            this.members.add(Member())
                        },
                        revisedFormation.apply {
                            this.add(existingSquad.members[0])
                            this.add(existingSquad.members[1])
                            this.add(Member())
                            this.add(Member())
                        },
                        nextRevisionNumber,
                        listOf(
                            SquadMemberHistory(existingSquad, revisedFormation[2], nextRevisionNumber, ADD),
                            SquadMemberHistory(existingSquad, revisedFormation[3], nextRevisionNumber, ADD)
                        )
                    )
                },

                Triple(
                    Squad(),
                    mutableListOf<Member>(),
                    5
                ).let { (existingSquad, revisedFormation, nextRevisionNumber) ->
                    arguments(
                        existingSquad.apply {
                            this.members.add(Member())
                            this.members.add(Member())
                            this.members.add(Member())
                            this.members.add(Member())
                        },
                        revisedFormation.apply {
                            this.add(existingSquad.members[0])
                            this.add(existingSquad.members[1])
                        },
                        nextRevisionNumber,
                        listOf(
                            SquadMemberHistory(existingSquad, existingSquad.members[2], nextRevisionNumber, REMOVE),
                            SquadMemberHistory(existingSquad, existingSquad.members[3], nextRevisionNumber, REMOVE)
                        )
                    )
                },

                Triple(
                    Squad(),
                    mutableListOf<Member>(),
                    6
                ).let { (existingSquad, revisedFormation, nextRevisionNumber) ->
                    arguments(
                        existingSquad.apply {
                            this.members.add(Member())
                            this.members.add(Member())
                        },
                        revisedFormation.apply {
                            this.add(existingSquad.members[0])
                            this.add(Member())
                        },
                        nextRevisionNumber,
                        listOf(
                            SquadMemberHistory(existingSquad, revisedFormation[1], nextRevisionNumber, ADD),
                            SquadMemberHistory(existingSquad, existingSquad.members[1], nextRevisionNumber, REMOVE),
                        )
                    )
                },
            )
        }
    }
}
