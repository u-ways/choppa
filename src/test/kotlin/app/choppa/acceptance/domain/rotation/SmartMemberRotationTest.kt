package app.choppa.acceptance.domain.rotation

import app.choppa.domain.chapter.Chapter
import app.choppa.domain.member.Member
import app.choppa.domain.rotation.smr.MoveToSquad
import app.choppa.domain.rotation.smr.SmartMemberRotation
import app.choppa.domain.squad.Squad
import app.choppa.support.base.Universe
import app.choppa.support.factory.ChapterFactory
import app.choppa.support.factory.MemberFactory
import app.choppa.support.factory.SquadFactory
import app.choppa.support.matchers.containsInAnyOrder
import com.natpryce.hamkrest.assertion.assertThat
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.Arguments.of
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

@Disabled
class SmartMemberRotationTest : Universe() {
    @ParameterizedTest
    @MethodSource("noRevisionHistoryScenarioArgs")
    fun `Given Tribe with W squads with newly created X members of which Y are in the developer chapter, when Smart Member Rotation is invoked to rotate Z amount of developer, it should create expected Tribe formation`(
        squads: List<Squad>,
        chapterToRotate: Chapter,
        squadsRevisionsAndMemberDuration: MutableList<Pair<Squad, List<Pair<Int, List<Member>>>>>,
        amountToRotate: Int,
        expectedMoveToSquads: List<MoveToSquad>,
    ) {
        val actualMoveToSquads = SmartMemberRotation(
            squads = squads,
            chapter = chapterToRotate,
            squadsRevisionsAndMemberDuration = squadsRevisionsAndMemberDuration,
            amount = amountToRotate
        ).invoke()

        assertThat(actualMoveToSquads, List<MoveToSquad>::containsInAnyOrder, expectedMoveToSquads)
    }

    @ParameterizedTest
    @MethodSource("withRevisionHistoryScenarioArgs")
    fun `Given Tribe with X squads with newly created Y members of which Z are in the developer chapter with several squad revisions, when Smart Member Rotation is invoked, it should create expected Tribe formation`(
        squads: List<Squad>,
        chapterToRotate: Chapter,
        squadsRevisionsAndMemberDuration: MutableList<Pair<Squad, List<Pair<Int, List<Member>>>>>,
        amountToRotate: Int,
        expectedMoveToSquads: List<MoveToSquad>,
    ) {
        val actualMoveToSquads = SmartMemberRotation(
            squads = squads,
            chapter = chapterToRotate,
            squadsRevisionsAndMemberDuration = squadsRevisionsAndMemberDuration,
            amount = amountToRotate
        ).invoke()

        assertThat(actualMoveToSquads, List<MoveToSquad>::containsInAnyOrder, expectedMoveToSquads)
    }

    companion object {
        @JvmStatic
        @Suppress("unused")
        fun noRevisionHistoryScenarioArgs(): Stream<Arguments> {
            val role = "developer"
            return Stream.of(
                // Scenario #1 - 2,4,2,2
                ChapterFactory.create(name = role).let { chapterToRotate ->
                    val squadAmount = 2
                    val membersAmountPerSquad = 4
                    val chapterToRotateMembersAmountPerSquad = 2
                    val squadsRevisionsAndMemberDuration = mutableListOf<Pair<Squad, List<Pair<Int, List<Member>>>>>()
                    val squads = SquadFactory.create(squadAmount).onEach {
                        it.members.addAll(
                            MemberFactory.create(
                                amount = chapterToRotateMembersAmountPerSquad,
                                sharedChapter = chapterToRotate
                            )
                        )
                        it.members.addAll(
                            MemberFactory.create(
                                amount = membersAmountPerSquad - chapterToRotateMembersAmountPerSquad
                            )
                        )
                        squadsRevisionsAndMemberDuration.add(it to listOf(0 to it.members))
                    }
                    val expected = mutableListOf<MoveToSquad>().apply {
                        add(MoveToSquad(member = squads[1].members[0], from = squads[1], to = squads[0]))
                        add(MoveToSquad(member = squads[0].members[0], from = squads[0], to = squads[1]))
                        add(MoveToSquad(member = squads[1].members[1], from = squads[1], to = squads[0]))
                        add(MoveToSquad(member = squads[0].members[1], from = squads[0], to = squads[1]))
                    }
                    val amountToRotate = 2
                    of(squads, chapterToRotate, squadsRevisionsAndMemberDuration, amountToRotate, expected)
                },
                // Scenario #2 - 2,10,5,3
                ChapterFactory.create(name = role).let { chapterToRotate ->
                    val squadAmount = 2
                    val membersAmountPerSquad = 10
                    val chapterToRotateMembersAmountPerSquad = 5
                    val squadsRevisionsAndMemberDuration = mutableListOf<Pair<Squad, List<Pair<Int, List<Member>>>>>()
                    val squads = SquadFactory.create(squadAmount).onEach {
                        it.members.addAll(
                            MemberFactory.create(
                                amount = chapterToRotateMembersAmountPerSquad,
                                sharedChapter = chapterToRotate
                            )
                        )
                        it.members.addAll(MemberFactory.create(amount = membersAmountPerSquad - chapterToRotateMembersAmountPerSquad))
                        squadsRevisionsAndMemberDuration.add(it to listOf(0 to it.members))
                    }
                    val expected = mutableListOf<MoveToSquad>().apply {
                        add(MoveToSquad(member = squads[1].members[0], from = squads[1], to = squads[0]))
                        add(MoveToSquad(member = squads[0].members[0], from = squads[0], to = squads[1]))
                        add(MoveToSquad(member = squads[1].members[1], from = squads[1], to = squads[0]))
                        add(MoveToSquad(member = squads[0].members[1], from = squads[0], to = squads[1]))
                        add(MoveToSquad(member = squads[1].members[2], from = squads[1], to = squads[0]))
                        add(MoveToSquad(member = squads[0].members[2], from = squads[0], to = squads[1]))
                    }
                    val amountToRotate = 3
                    of(squads, chapterToRotate, squadsRevisionsAndMemberDuration, amountToRotate, expected)
                },
                // Scenario #3 - 3,18,5,5
                ChapterFactory.create(name = role).let { chapterToRotate ->
                    val squadAmount = 3
                    val membersAmountPerSquad = 18
                    val chapterToRotateMembersAmountPerSquad = 5
                    val squadsRevisionsAndMemberDuration = mutableListOf<Pair<Squad, List<Pair<Int, List<Member>>>>>()
                    val squads = SquadFactory.create(squadAmount).onEach {
                        it.members.addAll(
                            MemberFactory.create(
                                amount = chapterToRotateMembersAmountPerSquad,
                                sharedChapter = chapterToRotate
                            )
                        )
                        it.members.addAll(MemberFactory.create(amount = membersAmountPerSquad - chapterToRotateMembersAmountPerSquad))
                        squadsRevisionsAndMemberDuration.add(it to listOf(0 to it.members))
                    }
                    val expected = mutableListOf<MoveToSquad>().apply {
                        add(MoveToSquad(member = squads[1].members[0], from = squads[1], to = squads[0]))
                        add(MoveToSquad(member = squads[0].members[0], from = squads[0], to = squads[1]))
                        add(MoveToSquad(member = squads[1].members[1], from = squads[1], to = squads[0]))
                        add(MoveToSquad(member = squads[0].members[1], from = squads[0], to = squads[1]))
                        add(MoveToSquad(member = squads[1].members[2], from = squads[1], to = squads[0]))
                        add(MoveToSquad(member = squads[0].members[2], from = squads[0], to = squads[1]))
                        add(MoveToSquad(member = squads[1].members[3], from = squads[1], to = squads[0]))
                        add(MoveToSquad(member = squads[0].members[3], from = squads[0], to = squads[1]))
                        add(MoveToSquad(member = squads[1].members[4], from = squads[1], to = squads[0]))
                        add(MoveToSquad(member = squads[0].members[4], from = squads[0], to = squads[1]))
                    }
                    val amountToRotate = 5
                    of(squads, chapterToRotate, squadsRevisionsAndMemberDuration, amountToRotate, expected)
                },
            )
        }

        @JvmStatic
        @Suppress("unused")
        fun withRevisionHistoryScenarioArgs(): Stream<Arguments> {
            val role = "Tester"
            return Stream.of(
                // Scenario #1 - 2,4,2,2
                ChapterFactory.create(name = role).let { chapterToRotate ->
                    val squadAmount = 2
                    val membersAmountPerSquad = 4
                    val chapterToRotateMembersAmountPerSquad = 2
                    val squadsRevisionsAndMemberDuration = mutableListOf<Pair<Squad, List<Pair<Int, List<Member>>>>>()
                    val squads = SquadFactory.create(squadAmount).onEachIndexed { index, squad ->
                        squad.members.addAll(
                            MemberFactory.create(
                                amount = chapterToRotateMembersAmountPerSquad,
                                sharedChapter = chapterToRotate
                            )
                        )
                        squad.members.addAll(
                            MemberFactory.create(
                                amount = membersAmountPerSquad - chapterToRotateMembersAmountPerSquad
                            )
                        )
                        squadsRevisionsAndMemberDuration.add(squad to listOf(index * 10 to squad.members))
                    }
                    val expected = mutableListOf<MoveToSquad>().apply {
                        add(MoveToSquad(member = squads[1].members[0], from = squads[1], to = squads[0]))
                        add(MoveToSquad(member = squads[0].members[0], from = squads[0], to = squads[1]))

                        add(MoveToSquad(member = squads[1].members[1], from = squads[1], to = squads[0]))
                        add(MoveToSquad(member = squads[0].members[1], from = squads[0], to = squads[1]))
                    }
                    val amountToRotate = 2
                    of(squads, chapterToRotate, squadsRevisionsAndMemberDuration, amountToRotate, expected)
                },
                // Scenario #2 - 2,10,5,3
                ChapterFactory.create(name = role).let { chapterToRotate ->
                    val squadAmount = 2
                    val membersAmountPerSquad = 10
                    val chapterToRotateMembersAmountPerSquad = 5
                    val squadsRevisionsAndMemberDuration = mutableListOf<Pair<Squad, List<Pair<Int, List<Member>>>>>()
                    val squads = SquadFactory.create(squadAmount).onEachIndexed { index, squad ->
                        squad.members.addAll(
                            MemberFactory.create(
                                amount = chapterToRotateMembersAmountPerSquad,
                                sharedChapter = chapterToRotate
                            )
                        )
                        squad.members.addAll(MemberFactory.create(amount = membersAmountPerSquad - chapterToRotateMembersAmountPerSquad))
                        squadsRevisionsAndMemberDuration.add(squad to listOf(index * 10 to squad.members))
                    }
                    val expected = mutableListOf<MoveToSquad>().apply {
                        add(MoveToSquad(member = squads[1].members[0], from = squads[1], to = squads[0]))
                        add(MoveToSquad(member = squads[0].members[0], from = squads[0], to = squads[1]))

                        add(MoveToSquad(member = squads[1].members[1], from = squads[1], to = squads[0]))
                        add(MoveToSquad(member = squads[0].members[1], from = squads[0], to = squads[1]))

                        add(MoveToSquad(member = squads[1].members[2], from = squads[1], to = squads[0]))
                        add(MoveToSquad(member = squads[0].members[2], from = squads[0], to = squads[1]))
                    }
                    val amountToRotate = 3
                    of(squads, chapterToRotate, squadsRevisionsAndMemberDuration, amountToRotate, expected)
                },
                // Scenario #3 - 3,18,5,5
                ChapterFactory.create(name = role).let { chapterToRotate ->
                    val squadAmount = 3
                    val membersAmountPerSquad = 18
                    val chapterToRotateMembersAmountPerSquad = 5
                    val squadsRevisionsAndMemberDuration = mutableListOf<Pair<Squad, List<Pair<Int, List<Member>>>>>()
                    val squads = SquadFactory.create(squadAmount).onEachIndexed { index, squad ->
                        squad.members.addAll(
                            MemberFactory.create(
                                amount = chapterToRotateMembersAmountPerSquad,
                                sharedChapter = chapterToRotate
                            )
                        )
                        squad.members.addAll(MemberFactory.create(amount = membersAmountPerSquad - chapterToRotateMembersAmountPerSquad))
                        squadsRevisionsAndMemberDuration.add(squad to listOf(index * 10 to squad.members))
                    }
                    val expected = mutableListOf<MoveToSquad>().apply {
                        add(MoveToSquad(member = squads[2].members[0], from = squads[2], to = squads[0]))
                        add(MoveToSquad(member = squads[0].members[0], from = squads[0], to = squads[2]))

                        add(MoveToSquad(member = squads[2].members[1], from = squads[2], to = squads[1]))
                        add(MoveToSquad(member = squads[1].members[0], from = squads[1], to = squads[2]))

                        add(MoveToSquad(member = squads[2].members[2], from = squads[2], to = squads[0]))
                        add(MoveToSquad(member = squads[0].members[1], from = squads[0], to = squads[2]))

                        add(MoveToSquad(member = squads[2].members[3], from = squads[2], to = squads[1]))
                        add(MoveToSquad(member = squads[1].members[1], from = squads[1], to = squads[2]))

                        add(MoveToSquad(member = squads[2].members[4], from = squads[2], to = squads[0]))
                        add(MoveToSquad(member = squads[0].members[2], from = squads[0], to = squads[2]))
                    }
                    val amountToRotate = 5
                    of(squads, chapterToRotate, squadsRevisionsAndMemberDuration, amountToRotate, expected)
                },
            )
        }
    }
}
