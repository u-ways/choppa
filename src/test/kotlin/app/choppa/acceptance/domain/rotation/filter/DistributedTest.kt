package app.choppa.acceptance.domain.rotation.filter

import app.choppa.domain.member.Member
import app.choppa.domain.rotation.filter.distributed
import app.choppa.support.factory.MemberFactory
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

internal class DistributedTest {

    /**
     *
     * | Test # | candidates              | Amount  | Expected           |
     * |:-------|:------------------------|:--------|:-------------------|
     * |  1     | []                      | 1       | []                 |
     * |  2     | [{M1}]                  | 1       | [{M1}]             |
     * |  3     | [{M1},{M2}]             | 2       | [{M1},{M2}]        |
     * |  4     | [{M1,M2},{M3}]          | 2       | [{M1},{M3}]        |
     * |  5     | [{M1},{M2,M3}]          | 3       | [{M1},{M2,M3}]     |
     * |  6     | [{M1,M2},{M3,M4,M5},{}] | 3       | [{M1},{M3},{}]   |
     */
    @ParameterizedTest
    @MethodSource("distributedTestArgs")
    fun `Given squad candidates, when rotation filter distributed is applied to collect an amount of candidates, then distributed filter should return expected candidates`(
        candidates: List<List<Member>>,
        amount: Int,
        expected: List<List<Member>>,
    ) {
        distributed(candidates, amount) shouldBeEqualTo expected
    }

    companion object {
        @JvmStatic
        fun distributedTestArgs(): Stream<Arguments?>? {
            return Stream.of(
                // Test 1
                listOf<List<Member>>().let { candidates ->
                    arguments(
                        candidates,
                        1,
                        emptyList<List<Member>>()
                    )
                },
                // Test 2
                listOf<List<Member>>(
                    MemberFactory.create(1),
                ).let { candidates ->
                    arguments(
                        candidates,
                        1,
                        listOf(
                            listOf(candidates[0][0])
                        )
                    )
                },
                // Test 3
                listOf<List<Member>>(
                    MemberFactory.create(1),
                    MemberFactory.create(1),
                ).let { candidates ->
                    arguments(
                        candidates,
                        2,
                        listOf(
                            listOf(candidates[0][0]),
                            listOf(candidates[1][0]),
                        )
                    )
                },
                // Test 4
                listOf<List<Member>>(
                    MemberFactory.create(2),
                    MemberFactory.create(1),
                ).let { candidates ->
                    arguments(
                        candidates,
                        2,
                        listOf(
                            listOf(candidates[0][0]),
                            listOf(candidates[1][0]),
                        )
                    )
                },
                // Test 5
                listOf<List<Member>>(
                    MemberFactory.create(1),
                    MemberFactory.create(2),
                ).let { candidates ->
                    arguments(
                        candidates,
                        3,
                        listOf(
                            listOf(candidates[0][0]),
                            listOf(candidates[1][0], candidates[1][1]),
                        )
                    )
                },
                // Test 6
                listOf<List<Member>>(
                    MemberFactory.create(2),
                    MemberFactory.create(3),
                    MemberFactory.create(0),
                ).let { candidates ->
                    arguments(
                        candidates,
                        3,
                        listOf(
                            listOf(candidates[0][0], candidates[0][1]),
                            listOf(candidates[1][0]),
                            listOf(),
                        )
                    )
                },
            )
        }
    }
}
