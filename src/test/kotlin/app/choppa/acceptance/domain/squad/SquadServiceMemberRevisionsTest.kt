package app.choppa.acceptance.domain.squad

import app.choppa.domain.member.Member
import app.choppa.domain.member.MemberService
import app.choppa.domain.squad.Squad
import app.choppa.domain.squad.SquadRepository
import app.choppa.domain.squad.SquadService
import app.choppa.domain.squad.history.RevisionType.ADD
import app.choppa.domain.squad.history.RevisionType.REMOVE
import app.choppa.domain.squad.history.SquadMemberHistory
import app.choppa.domain.squad.history.SquadMemberHistoryService
import app.choppa.support.factory.SquadMemberHistoryFactory
import app.choppa.support.matchers.containsInAnyOrder
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import io.mockk.every
import io.mockk.mockkClass
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.Optional.of
import java.util.UUID.randomUUID
import java.util.stream.Stream

internal class SquadServiceMemberRevisionsTest {
    private lateinit var repository: SquadRepository
    private lateinit var memberService: MemberService
    private lateinit var squadMemberHistoryService: SquadMemberHistoryService
    private lateinit var service: SquadService

    @BeforeEach
    internal fun setUp() {
        repository = mockkClass(SquadRepository::class)
        memberService = mockkClass(MemberService::class)
        squadMemberHistoryService = mockkClass(SquadMemberHistoryService::class)
        service = SquadService(repository, memberService, squadMemberHistoryService)
    }

    @Test
    fun `Given no revisions, when service requests all revisions, then service should return empty list of members`() {
        val existingSquad = Squad()

        every {
            repository.findById(existingSquad.id)
        } returns of(existingSquad)

        every {
            squadMemberHistoryService.findAllSquadMemberRevisions(existingSquad)
        } returns emptyList()

        val actualSquadMembersRevisions = service.findAllSquadMembersRevisions(existingSquad.id)

        assertThat(actualSquadMembersRevisions, equalTo(emptyList()))
    }

    /**
     *  R0 = Squad Revision number 0
     * +M# = ADD Member #
     * -M# = REMOVE Member #
     *
     * | Test # | Revision Scenario          | Expected Result of Req Revision #        |
     * |:-------|:---------------------------|:-----------------------------------------|
     * |  1     | R0+M1                      | [<R0,{M1}>]                              |
     * |  2     | R0+M1+M2                   | [<R0,{M1,M2}>]                           |
     * |  3     | R0+M1 => R1+M2             | [<R0,{M1}>,<R1,{M1,M2}>]                 |
     * |  4     | R0+M1 => R1-M1             | [<R0,{M1}>,<R1,{}>]                      |
     * |  5     | R0+M1 => R1-M1+M2          | [<R0,{M1}>,<R1,{M2}>]                    |
     * |  6     | R0+M1 => R1+M2+M3          | [<R0,{M1}>,<R1,{M1,M2,M3}>]              |
     * |  7     | R0+M1 => R1+M2 => R2+M3    | [<R0,{M1}>,<R1,{M1,M2}>,<R2,{M1,M2,M3}>] |
     * |  8     | R0+M1 => R1-M1 => R2+M1    | [<R0,{M1}>,<R1,{}>,<R2,{M1}>]            |
     */
    @ParameterizedTest
    @MethodSource("findAllRevisionsTestArgs")
    fun `Given revision scenario, when service requests all revisions, then service should return a pair of revision number and correct squad formation at said revision`(
        revisionScenario: List<SquadMemberHistory>,
        expectedResult: List<List<Member>>
    ) {
        val existingSquad = revisionScenario.first().squad
        val relatedMembers = revisionScenario.distinctBy { it.member }.map { it.member }

        every {
            repository.findById(existingSquad.id)
        } returns of(existingSquad)

        relatedMembers.forEach {
            every { memberService.find(it.id) } returns it
        }

        every {
            squadMemberHistoryService.findAllSquadMemberRevisions(existingSquad)
        } returns revisionScenario.reversed()

        val actualSquadMembersRevisions = service.findAllSquadMembersRevisions(existingSquad.id)

        actualSquadMembersRevisions.forEachIndexed { index, actualRevisionMembers: List<Member> ->
            actualRevisionMembers.size shouldBeEqualTo expectedResult[index].size
            assertThat(actualRevisionMembers, List<Member>::containsInAnyOrder, expectedResult[index])
        }
    }

    /**
     * R = Revision (i.e. R0 = Squad Revision number 0)
     * M = Member   (i.e. M1 = Member number 1)
     *
     * | Test # | Req Revision #     | Revision Scenario                             | Expected Result of Req Revision # |
     * |:-------|:-------------------|:----------------------------------------------|-----------------------------------|
     * |  1     | Revert to rev # -1 | R0 ADD M1                                     | Empty List of Members             |
     * |  2     | Revert to rev #  0 | R0 ADD M1                                     | [ M1 ]                            |
     * |  3     | Revert to rev #  1 | R0 ADD M1, R1 REMOVE M1                       | Empty List of Members             |
     * |  4     | Revert to rev #  0 | R0 ADD M1, R1 ADD M2                          | [ M1 ]                            |
     * |  5     | Revert to rev #  1 | R0 ADD M1, R1 ADD M2                          | [ M1, M2 ]                        |
     * |  6     | Revert to rev #  1 | R0 ADD M1, R1 ADD M2, R2 REMOVE M1            | [ M1, M2 ]                        |
     * |  7     | Revert to rev #  1 | R0 ADD M1, R1 ADD M2, R2 REMOVE M2            | [ M1, M2 ]                        |
     * |  8     | Revert to rev #  2 | R0 ADD M1, R1 ADD M2, R2 REMOVE M1            | [ M2 ]                            |
     * |  9     | Revert to rev #  2 | R0 ADD M1, R1 ADD M2, R2 REMOVE M2            | [ M1 ]                            |
     * |  10    | Revert to rev #  0 | R0 ADD M1, R1 ADD M2, R2 REMOVE M2, R3 ADD M3 | [ M1 ]                            |
     */
    @ParameterizedTest
    @MethodSource("revertToTestArgs")
    fun `Given revision scenario, when service requests squad formation to revision number, then service should return correct members formation`(
        requestedRevision: Int,
        revisionScenario: List<SquadMemberHistory>,
        expectedResult: List<Member>
    ) {
        val existingSquad = revisionScenario.first().squad
        val relatedMembers = revisionScenario.distinctBy { it.member }.map { it.member }

        every {
            repository.findById(existingSquad.id)
        } returns of(existingSquad)

        relatedMembers.forEach {
            every { memberService.find(it.id) } returns it
        }

        every {
            squadMemberHistoryService.findSquadMemberRevisionsAfter(
                existingSquad,
                requestedRevision
            )
        } returns revisionScenario.subList(requestedRevision + 1, revisionScenario.size).reversed()

        val actualMembersAtRequestedRevision = service
            .revertSquadMembersFormationTo(existingSquad.id, requestedRevision)
            .members

        actualMembersAtRequestedRevision.size shouldBeEqualTo expectedResult.size
        assertThat(actualMembersAtRequestedRevision, List<Member>::containsInAnyOrder, expectedResult)
    }

    @Test
    fun `Given existing entity with no member revisions, when service reverts by an invalid number, then service should return same list of members`() {
        val id = randomUUID()
        val existingEntity = Squad(id)

        val relatedSquadMemberRevisions = SquadMemberHistoryFactory.create(0)

        every { repository.findById(id) } returns of(existingEntity)

        every {
            squadMemberHistoryService.findLastNSquadMemberRevisions(
                existingEntity,
                0
            )
        } returns relatedSquadMemberRevisions

        val result = service.revertSquadMembersFormationBy(id, 0)

        existingEntity.members shouldBeEqualTo result.members
    }

    /**
     * R = Revision (i.e. R0 = Squad revision number 0)
     * M = Member   (i.e. M1 = Member number 1)
     *
     * | Test # | Req Revision #    | Revision Scenario                     | Expected Result of Req Revision # |
     * |:-------|:------------------|:--------------------------------------|-----------------------------------|
     * |  1     | Revert by 0 revs  | R0 ADD M1                             | [ M1 ]                            |
     * |  2     | Revert by 1 revs  | R0 ADD M1                             | Empty List of Members             |
     * |  3     | Revert by 1 revs  | R0 ADD M1, R1 REMOVE M1               | [ M1 ]                            |
     * |  4     | Revert by 2 revs  | R0 ADD M1, R1 ADD M2                  | Empty List of Members             |
     * |  5     | Revert by 1 revs  | R0 ADD M1, R1 ADD M2, R2 REMOVE M1    | [ M1, M2 ]                        |
     * |  6     | Revert by 2 revs  | R0 ADD M1, R1 ADD M2, R2 REMOVE M2    | [ M1 ]                            |
     * |  7     | Revert by 3 revs  | R0 ADD M1, R1 ADD M2, R2 REMOVE M1    | Empty List of Members             |
     */
    @ParameterizedTest
    @MethodSource("revertByTestArgs")
    fun `Given revision scenario, when service reverts squad by amountOfRevisionToUndo, then service should return correct members formation`(
        amountOfRevisionToUndo: Int,
        revisionScenario: List<SquadMemberHistory>,
        expectedResult: List<Member>
    ) {
        val existingSquad = revisionScenario.first().squad
        val relatedMembers = revisionScenario.distinctBy { it.member }.map { it.member }

        every {
            repository.findById(existingSquad.id)
        } returns of(existingSquad)

        relatedMembers.forEach {
            every { memberService.find(it.id) } returns it
        }

        every {
            squadMemberHistoryService.findLastNSquadMemberRevisions(existingSquad, amountOfRevisionToUndo)
        } returns revisionScenario.takeLast(amountOfRevisionToUndo).reversed()

        val actualMembersAtRequestedRevision = service
            .revertSquadMembersFormationBy(existingSquad.id, amountOfRevisionToUndo)
            .members

        actualMembersAtRequestedRevision.size shouldBeEqualTo expectedResult.size
        assertThat(actualMembersAtRequestedRevision, List<Member>::containsInAnyOrder, expectedResult)
    }

    companion object {
        @JvmStatic
        fun revertToTestArgs(): Stream<Arguments?>? {
            val existingSquad = Squad()
            return Stream.of(
                arguments(
                    -1,
                    listOf(
                        SquadMemberHistory(
                            existingSquad,
                            Member(),
                            0,
                            ADD
                        )
                    ),
                    emptyList<Member>()
                ),

                Pair(Squad(), mutableListOf<Member>()).let { (existingSquad, expectedResult) ->
                    arguments(
                        0,
                        listOf(
                            SquadMemberHistory(
                                existingSquad,
                                Member().apply {
                                    existingSquad.members.add(this)
                                    expectedResult.add(this)
                                },
                                0,
                                ADD
                            )
                        ),
                        expectedResult
                    )
                },

                Pair(Squad(), mutableListOf<Member>()).let { (existingSquad, expectedResult) ->
                    arguments(
                        1,
                        listOf(
                            SquadMemberHistory(
                                existingSquad,
                                Member().apply {
                                    existingSquad.members.add(this)
                                    expectedResult.add(this)
                                },
                                0,
                                ADD
                            ),
                            SquadMemberHistory(
                                existingSquad,
                                expectedResult[0].apply {
                                    existingSquad.members.remove(this)
                                    expectedResult.remove(this)
                                },
                                1,
                                REMOVE
                            ),
                        ),
                        expectedResult
                    )
                },

                Pair(Squad(), mutableListOf<Member>()).let { (existingSquad, expectedResult) ->
                    arguments(
                        0,
                        listOf(
                            SquadMemberHistory(
                                existingSquad,
                                Member().apply {
                                    existingSquad.members.add(this)
                                    expectedResult.add(this)
                                },
                                0,
                                ADD
                            ),
                            SquadMemberHistory(
                                existingSquad,
                                Member().apply {
                                    existingSquad.members.add(this)
                                },
                                1,
                                ADD
                            ),
                        ),
                        expectedResult
                    )
                },

                Pair(Squad(), mutableListOf<Member>()).let { (existingSquad, expectedResult) ->
                    arguments(
                        1,
                        listOf(
                            SquadMemberHistory(
                                existingSquad,
                                Member().apply {
                                    existingSquad.members.add(this)
                                    expectedResult.add(this)
                                },
                                0,
                                ADD
                            ),
                            SquadMemberHistory(
                                existingSquad,
                                Member().apply {
                                    existingSquad.members.add(this)
                                    expectedResult.add(this)
                                },
                                1,
                                ADD
                            ),
                        ),
                        expectedResult
                    )
                },

                Pair(Squad(), mutableListOf<Member>()).let { (existingSquad, expectedResult) ->
                    arguments(
                        1,
                        listOf(
                            SquadMemberHistory(
                                existingSquad,
                                Member().apply {
                                    existingSquad.members.add(this)
                                    expectedResult.add(this)
                                },
                                0,
                                ADD
                            ),
                            SquadMemberHistory(
                                existingSquad,
                                Member().apply {
                                    existingSquad.members.add(this)
                                    expectedResult.add(this)
                                },
                                1,
                                ADD
                            ),

                            SquadMemberHistory(
                                existingSquad,
                                expectedResult[0].apply {
                                    existingSquad.members.remove(this)
                                },
                                2,
                                REMOVE
                            ),
                        ),
                        expectedResult
                    )
                },

                Pair(Squad(), mutableListOf<Member>()).let { (existingSquad, expectedResult) ->
                    arguments(
                        1,
                        listOf(
                            SquadMemberHistory(
                                existingSquad,
                                Member().apply {
                                    existingSquad.members.add(this)
                                    expectedResult.add(this)
                                },
                                0,
                                ADD
                            ),
                            SquadMemberHistory(
                                existingSquad,
                                Member().apply {
                                    existingSquad.members.add(this)
                                    expectedResult.add(this)
                                },
                                1,
                                ADD
                            ),

                            SquadMemberHistory(
                                existingSquad,
                                expectedResult[1].apply {
                                    existingSquad.members.remove(this)
                                },
                                2,
                                REMOVE
                            ),
                        ),
                        expectedResult
                    )
                },

                Pair(Squad(), mutableListOf<Member>()).let { (existingSquad, expectedResult) ->
                    arguments(
                        2,
                        listOf(
                            SquadMemberHistory(
                                existingSquad,
                                Member().apply {
                                    existingSquad.members.add(this)
                                    expectedResult.add(this)
                                },
                                0,
                                ADD
                            ),
                            SquadMemberHistory(
                                existingSquad,
                                Member().apply {
                                    existingSquad.members.add(this)
                                    expectedResult.add(this)
                                },
                                1,
                                ADD
                            ),

                            SquadMemberHistory(
                                existingSquad,
                                expectedResult[0].apply {
                                    existingSquad.members.remove(this)
                                    expectedResult.remove(this)
                                },
                                2,
                                REMOVE
                            ),
                        ),
                        expectedResult
                    )
                },

                Pair(Squad(), mutableListOf<Member>()).let { (existingSquad, expectedResult) ->
                    arguments(
                        2,
                        listOf(
                            SquadMemberHistory(
                                existingSquad,
                                Member().apply {
                                    existingSquad.members.add(this)
                                    expectedResult.add(this)
                                },
                                0,
                                ADD
                            ),
                            SquadMemberHistory(
                                existingSquad,
                                Member().apply {
                                    existingSquad.members.add(this)
                                    expectedResult.add(this)
                                },
                                1,
                                ADD
                            ),

                            SquadMemberHistory(
                                existingSquad,
                                expectedResult[1].apply {
                                    existingSquad.members.remove(this)
                                    expectedResult.remove(this)
                                },
                                2,
                                REMOVE
                            ),
                        ),
                        expectedResult
                    )
                },

                Pair(Squad(), mutableListOf<Member>()).let { (existingSquad, expectedResult) ->
                    arguments(
                        0,
                        listOf(
                            SquadMemberHistory(
                                existingSquad,
                                Member().apply {
                                    existingSquad.members.add(this)
                                    expectedResult.add(this)
                                },
                                0,
                                ADD
                            ),
                            SquadMemberHistory(
                                existingSquad,
                                Member().apply {
                                    existingSquad.members.add(this)
                                },
                                1,
                                ADD
                            ),
                            SquadMemberHistory(
                                existingSquad,
                                existingSquad.members[1].apply {
                                    existingSquad.members.remove(this)
                                },
                                2,
                                REMOVE
                            ),
                            SquadMemberHistory(
                                existingSquad,
                                Member().apply {
                                    existingSquad.members.add(this)
                                },
                                3,
                                ADD
                            ),
                        ),
                        expectedResult
                    )
                },
            )
        }

        @JvmStatic
        fun revertByTestArgs(): Stream<Arguments?>? {
            val existingSquad = Squad()
            return Stream.of(
                Pair(Squad(), mutableListOf<Member>()).let { (existingSquad, expectedResult) ->
                    arguments(
                        0,
                        listOf(
                            SquadMemberHistory(
                                existingSquad,
                                Member().apply {
                                    existingSquad.members.add(this)
                                    expectedResult.add(this)
                                },
                                0,
                                ADD
                            )
                        ),
                        expectedResult
                    )
                },

                Pair(Squad(), mutableListOf<Member>()).let { (existingSquad, expectedResult) ->
                    arguments(
                        1,
                        listOf(
                            SquadMemberHistory(
                                existingSquad,
                                Member().apply {
                                    existingSquad.members.add(this)
                                },
                                0,
                                ADD
                            )
                        ),
                        expectedResult
                    )
                },

                Pair(Squad(), mutableListOf<Member>()).let { (existingSquad, expectedResult) ->
                    arguments(
                        1,
                        listOf(
                            SquadMemberHistory(
                                existingSquad,
                                Member().apply {
                                    existingSquad.members.add(this)
                                    expectedResult.add(this)
                                },
                                0,
                                ADD
                            ),
                            SquadMemberHistory(
                                existingSquad,
                                expectedResult[0].apply {
                                    existingSquad.members.remove(this)
                                },
                                1,
                                REMOVE
                            ),
                        ),
                        expectedResult
                    )
                },

                Pair(Squad(), mutableListOf<Member>()).let { (existingSquad, expectedResult) ->
                    arguments(
                        2,
                        listOf(
                            SquadMemberHistory(
                                existingSquad,
                                Member().apply {
                                    existingSquad.members.add(this)
                                },
                                0,
                                ADD
                            ),
                            SquadMemberHistory(
                                existingSquad,
                                Member().apply {
                                    existingSquad.members.add(this)
                                },
                                1,
                                ADD
                            ),
                        ),
                        expectedResult
                    )
                },

                Pair(Squad(), mutableListOf<Member>()).let { (existingSquad, expectedResult) ->
                    arguments(
                        1,
                        listOf(
                            SquadMemberHistory(
                                existingSquad,
                                Member().apply {
                                    expectedResult.add(this)
                                },
                                0,
                                ADD
                            ),
                            SquadMemberHistory(
                                existingSquad,
                                Member().apply {
                                    existingSquad.members.add(this)
                                    expectedResult.add(this)
                                },
                                1,
                                ADD
                            ),
                            SquadMemberHistory(
                                existingSquad,
                                expectedResult[0],
                                2,
                                REMOVE
                            ),
                        ),
                        expectedResult
                    )
                },

                Pair(Squad(), mutableListOf<Member>()).let { (existingSquad, expectedResult) ->
                    arguments(
                        2,
                        listOf(
                            SquadMemberHistory(
                                existingSquad,
                                Member().apply {
                                    existingSquad.members.add(this)
                                    expectedResult.add(this)
                                },
                                0,
                                ADD
                            ),
                            SquadMemberHistory(
                                existingSquad,
                                Member().apply {
                                    existingSquad.members.add(this)
                                },
                                1,
                                ADD
                            ),

                            SquadMemberHistory(
                                existingSquad,
                                existingSquad.members[1].apply {
                                    existingSquad.members.remove(this)
                                },
                                2,
                                REMOVE
                            ),
                        ),
                        expectedResult
                    )
                },

                Pair(Squad(), mutableListOf<Member>()).let { (existingSquad, expectedResult) ->
                    arguments(
                        3,
                        listOf(
                            SquadMemberHistory(
                                existingSquad,
                                Member().apply {
                                    existingSquad.members.add(this)
                                },
                                0,
                                ADD
                            ),
                            SquadMemberHistory(
                                existingSquad,
                                Member().apply {
                                    existingSquad.members.add(this)
                                },
                                1,
                                ADD
                            ),

                            SquadMemberHistory(
                                existingSquad,
                                existingSquad.members[0].apply {
                                    existingSquad.members.remove(this)
                                },
                                2,
                                REMOVE
                            ),
                        ),
                        expectedResult
                    )
                },
            )
        }

        @JvmStatic
        fun findAllRevisionsTestArgs(): Stream<Arguments?>? {
            val existingSquad = Squad()
            return Stream.of(
                // TEST 1
                Pair(
                    Squad(),
                    mutableListOf(
                        mutableListOf(Member()),
                    )
                ).let { (existingSquad, expectedResult) ->
                    arguments(
                        listOf(
                            SquadMemberHistory(
                                existingSquad,
                                expectedResult[0][0].apply {
                                    existingSquad.members.add(this)
                                },
                                0,
                                ADD
                            )
                        ),
                        expectedResult
                    )
                },

                // TEST 2
                Pair(
                    Squad(),
                    mutableListOf<MutableList<Member>>()
                ).let { (existingSquad, expectedResult) ->
                    arguments(
                        listOf(
                            SquadMemberHistory(
                                existingSquad,
                                Member().apply {
                                    expectedResult.add(mutableListOf(this))
                                },
                                0,
                                ADD
                            ),
                            SquadMemberHistory(
                                existingSquad,
                                Member().apply {
                                    expectedResult[0].add(this)
                                },
                                0,
                                ADD
                            ),
                        ),
                        expectedResult
                    )
                },

                // TEST 3
                Pair(
                    Squad(),
                    mutableListOf<MutableList<Member>>()
                ).let { (existingSquad, expectedResult) ->
                    arguments(
                        listOf(
                            SquadMemberHistory(
                                existingSquad,
                                Member().apply {
                                    expectedResult.add(mutableListOf(this))
                                },
                                0,
                                ADD
                            ),
                            SquadMemberHistory(
                                existingSquad,
                                Member().apply {
                                    expectedResult.add(
                                        mutableListOf(
                                            expectedResult[0][0],
                                            this
                                        )
                                    )
                                },
                                1,
                                ADD
                            ),
                        ),
                        expectedResult
                    )
                },

                // TEST 4
                Pair(
                    Squad(),
                    mutableListOf<MutableList<Member>>()
                ).let { (existingSquad, expectedResult) ->
                    arguments(
                        listOf(
                            SquadMemberHistory(
                                existingSquad,
                                Member().apply {
                                    expectedResult.add(mutableListOf(this))
                                },
                                0,
                                ADD
                            ),
                            SquadMemberHistory(
                                existingSquad,
                                expectedResult[0][0].apply {
                                    expectedResult.add(mutableListOf())
                                },
                                1,
                                REMOVE
                            ),
                        ),
                        expectedResult
                    )
                },

                // TEST 5
                Pair(
                    Squad(),
                    mutableListOf<MutableList<Member>>()
                ).let { (existingSquad, expectedResult) ->
                    arguments(
                        listOf(
                            SquadMemberHistory(
                                existingSquad,
                                Member().apply {
                                    expectedResult.add(mutableListOf(this))
                                },
                                0,
                                ADD
                            ),
                            SquadMemberHistory(
                                existingSquad,
                                expectedResult[0][0],
                                1,
                                REMOVE
                            ),
                            SquadMemberHistory(
                                existingSquad,
                                Member().apply {
                                    expectedResult.add(mutableListOf(this))
                                },
                                1,
                                ADD
                            ),
                        ),
                        expectedResult
                    )
                },

                // TEST 6
                Pair(
                    Squad(),
                    mutableListOf<MutableList<Member>>()
                ).let { (existingSquad, expectedResult) ->
                    arguments(
                        listOf(
                            SquadMemberHistory(
                                existingSquad,
                                Member().apply {
                                    expectedResult.add(mutableListOf(this))
                                },
                                0,
                                ADD
                            ),
                            SquadMemberHistory(
                                existingSquad,
                                Member().apply {
                                    expectedResult.add(
                                        mutableListOf(
                                            expectedResult[0][0],
                                            this
                                        )
                                    )
                                },
                                1,
                                ADD
                            ),
                            SquadMemberHistory(
                                existingSquad,
                                Member().apply {
                                    expectedResult[1].add(this)
                                },
                                1,
                                ADD
                            ),
                        ),
                        expectedResult
                    )
                },

                // TEST 7
                Pair(
                    Squad(),
                    mutableListOf<MutableList<Member>>()
                ).let { (existingSquad, expectedResult) ->
                    arguments(
                        listOf(
                            SquadMemberHistory(
                                existingSquad,
                                Member().apply {
                                    expectedResult.add(mutableListOf(this))
                                },
                                0,
                                ADD
                            ),
                            SquadMemberHistory(
                                existingSquad,
                                Member().apply {
                                    expectedResult.add(
                                        mutableListOf(
                                            expectedResult[0][0],
                                            this
                                        )
                                    )
                                },
                                1,
                                ADD
                            ),
                            SquadMemberHistory(
                                existingSquad,
                                Member().apply {
                                    expectedResult.add(
                                        mutableListOf(
                                            expectedResult[1][0],
                                            expectedResult[1][1],
                                            this
                                        )
                                    )
                                },
                                2,
                                ADD
                            ),
                        ),
                        expectedResult
                    )
                },

                // TEST 8
                Pair(
                    Squad(),
                    mutableListOf<MutableList<Member>>()
                ).let { (existingSquad, expectedResult) ->
                    arguments(
                        listOf(
                            SquadMemberHistory(
                                existingSquad,
                                Member().apply {
                                    expectedResult.add(mutableListOf(this))
                                },
                                0,
                                ADD
                            ),
                            SquadMemberHistory(
                                existingSquad,
                                expectedResult[0][0].apply {
                                    expectedResult.add(mutableListOf())
                                },
                                1,
                                REMOVE
                            ),
                            SquadMemberHistory(
                                existingSquad,
                                expectedResult[0][0].apply {
                                    expectedResult.add(mutableListOf(this))
                                },
                                2,
                                ADD
                            ),
                        ),
                        expectedResult
                    )
                },
            )
        }
    }
}
