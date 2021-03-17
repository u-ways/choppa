package app.choppa.acceptance.domain.rotation

import app.choppa.domain.member.Member
import app.choppa.domain.rotation.smr.SmartMemberRotation
import app.choppa.domain.squad.Squad
import app.choppa.domain.tribe.Tribe
import app.choppa.support.base.Universe
import app.choppa.support.factory.ChapterFactory
import app.choppa.support.factory.MemberFactory
import app.choppa.support.factory.SquadFactory
import app.choppa.support.factory.TribeFactory
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments.of
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class SmartMemberRotationTest : Universe() {

    @Test
    fun `2 squads, 4 members, swap 2 members without history`() {
        val squadAmount = 2
        val memberAmount = 4
        val developerChapterAmount = 2
        val developersToRotateAmount = 2
        val expected = mutableListOf<MutableList<Member>>()
        val squadsRevisionsAndMemberDuration = mutableListOf<Pair<Squad, List<Pair<Int, List<Member>>>>>()

        val developerChapter = ChapterFactory.create(name = "developer")

        var squadId = 1
        var memberId = 1
        val tribe = TribeFactory.create(
            squads = (0 until squadAmount).map {
                SquadFactory.create(name = "SQ" + squadId++)
            }.onEachIndexed { index, squad ->
                (0 until developerChapterAmount).forEach { x -> squad.members.add(MemberFactory.create(name = "" + memberId++, chapter = developerChapter)) }
                squad.members.addAll(MemberFactory.create(amount = memberAmount - developerChapterAmount))
                squadsRevisionsAndMemberDuration.add(squad to listOf((index * 1000) to squad.members))
            }.toMutableList()
        )

        tribe.squads.forEach { expected.add(it.members.toMutableList()) }
        expected[1].add(expected[0].removeAt(0))
        expected[1].add(expected[0].removeAt(0))
        expected[0].add(expected[1].removeAt(0))
        expected[0].add(expected[1].removeAt(0))

        SmartMemberRotation(
            squads = tribe.squads,
            amount = developersToRotateAmount,
            chapter = developerChapter,
            squadsRevisionsAndMemberDuration = squadsRevisionsAndMemberDuration
        ).invoke().onEach {
            tribe.apply {
                squads.apply {
                    find { squad -> squad == it.from }.apply { this?.members?.remove(it.member) }
                    find { squad -> squad == it.to }.apply { this?.members?.add(it.member) }
                }
            }
        }

        tribe.squads.map { x -> x.members } shouldBeEqualTo expected
    }

    @Test
    fun `with varying history`() {
        val squadAmount = 3
        val memberAmount = 4
        val developerChapterAmount = 2
        val developersToRotateAmount = 2
        val expected = mutableListOf<MutableList<Member>>()
        val squadsRevisionsAndMemberDuration = mutableListOf<Pair<Squad, List<Pair<Int, List<Member>>>>>()

        val developerChapter = ChapterFactory.create(name = "developer")

        var squadId = 1
        var memberId = 1
        val tribe = TribeFactory.create(
            squads = (0 until squadAmount).map {
                SquadFactory.create(name = "SQ" + squadId++)
            }.onEachIndexed { index, squad ->
                (0 until developerChapterAmount).forEach { x -> squad.members.add(MemberFactory.create(name = "" + memberId++, chapter = developerChapter)) }
                squad.members.addAll(MemberFactory.create(amount = memberAmount - developerChapterAmount))
                squadsRevisionsAndMemberDuration.add(squad to listOf((index * 1000) to squad.members))
            }.toMutableList()
        )

        tribe.squads.forEach { expected.add(it.members.toMutableList()) }
        expected[0].add(expected[2].removeAt(0))
        expected[1].add(expected[2].removeAt(0))
        expected[2].add(expected[0].removeAt(0))
        expected[2].add(expected[1].removeAt(0))

        debug(tribe)

        SmartMemberRotation(
            squads = tribe.squads,
            amount = developersToRotateAmount,
            chapter = developerChapter,
            squadsRevisionsAndMemberDuration = squadsRevisionsAndMemberDuration
        ).invoke().onEach {
            tribe.apply {
                squads.apply {
                    find { squad -> squad == it.from }.apply { this?.members?.remove(it.member) }
                    find { squad -> squad == it.to }.apply { this?.members?.add(it.member) }
                }
            }
        }
        debug(tribe)

        tribe.squads.map { x -> x.members } shouldBeEqualTo expected
    }

    @ParameterizedTest(name = "Given Tribe with {0} squads with newly created {1} members of which {2} are in the developer chapter, when Smart Member Rotation is invoked to rotate {3} amount of developer, it should create expected Tribe formation")
    @MethodSource("noRevisionHistoryScenarioArgs")
    fun `Given Tribe with W squads with newly created X members of which Y are in the developer chapter, when Smart Member Rotation is invoked to rotate Z amount of developer, it should create expected Tribe formation`(
        squadAmount: Int,
        memberAmount: Int,
        developerChapterAmount: Int,
        developersToRotateAmount: Int,
        expected: List<Squad>,
    ) {
        val developerChapter = ChapterFactory.create(name = "developer")

        val tribe = TribeFactory.create(
            squads = SquadFactory.create(squadAmount).onEach {
                it.members.addAll(MemberFactory.create(amount = memberAmount - developerChapterAmount))
                it.members.addAll(MemberFactory.create(amount = developerChapterAmount))
            }
        )

        SmartMemberRotation(
            squads = tribe.squads,
            amount = developersToRotateAmount,
            chapter = developerChapter,
            squadsRevisionsAndMemberDuration = emptyList()
        ).invoke().onEach {
            tribe.apply {
                squads.apply {
                    find { squad -> squad == it.from }.apply { this?.members?.remove(it.member) }
                    find { squad -> squad == it.to }.apply { this?.members?.add(it.member) }
                }
            }
        }

        tribe.squads shouldBeEqualTo expected
    }

    @ParameterizedTest(name = "Given Tribe with {0} squads with newly created {1} members of which {2} are in the developer chapter with several squad revisions, when Smart Member Rotation is invoked, it should create expected Tribe formation")
    @MethodSource("withRevisionHistoryScenarioArgs")
    fun `Given Tribe with X squads with newly created Y members of which Z are in the developer chapter with several squad revisions, when Smart Member Rotation is invoked, it should create expected Tribe formation`(
        squadAmount: Int,
        memberAmount: Int,
        developerChapterAmount: Int,
        developersToRotateAmount: Int,
        squadsRevisionsAndMemberDuration: List<Pair<Squad, List<Pair<Int, List<Member>>>>>,
        expected: List<Squad>,
    ) {
        val developerChapter = ChapterFactory.create(name = "developer")

        val tribe = TribeFactory.create(
            squads = SquadFactory.create(squadAmount).onEach {
                it.members.addAll(MemberFactory.create(amount = memberAmount - developerChapterAmount))
                it.members.addAll(MemberFactory.create(amount = developerChapterAmount))
            }
        )

        SmartMemberRotation(
            squads = tribe.squads,
            amount = developersToRotateAmount,
            chapter = developerChapter,
            squadsRevisionsAndMemberDuration = squadsRevisionsAndMemberDuration
        ).invoke().onEach {
            tribe.apply {
                squads.apply {
                    find { squad -> squad == it.from }.apply { this?.members?.remove(it.member) }
                    find { squad -> squad == it.to }.apply { this?.members?.add(it.member) }
                }
            }
        }

        tribe.squads shouldBeEqualTo expected
    }

    companion object {
//        @JvmStatic
//        fun noRevisionHistoryScenarioArgs() = Stream.of(
//
//            of(
//                // 1. Tribe to rotate
//                // 2. The chapter selected
//                // 3. The history
//                // 4. THe expected output
//            )
//            of(
//                2,
//                4,
//                2,
//                2,
//                mutableListOf<Pair<Squad, List<Pair<Int, List<Member>>>>>().apply {
//
//                },
//                emptyList<Squad>()
//            ),
//            of(2, 10, 5, 2, emptyList<Pair<Squad, List<Pair<Int, List<Member>>>>>(), emptyList<Squad>()),
//            of(3, 18, 5, 2, emptyList<Pair<Squad, List<Pair<Int, List<Member>>>>>(), emptyList<Squad>()),
//        )

        @JvmStatic
        fun withRevisionHistoryScenarioArgs() = Stream.of(
            of(2, 4, 2),
            of(2, 10, 5),
            of(3, 18, 5),
        )
    }

    private fun debug(tribe: Tribe) {
        tribe.squads.forEach { squad ->
            println(squad.name)
            println(squad.members.joinToString(",") { it.name })
        }
    }
}
