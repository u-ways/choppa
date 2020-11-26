package org.choppa.acceptance.utils

import org.amshove.kluent.shouldBeEqualTo
import org.choppa.domain.base.BaseModel
import org.choppa.utils.QueryComponent
import org.choppa.utils.ReverseRouter.Companion.queryComponent
import org.choppa.utils.ReverseRouter.Companion.route
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import java.util.UUID
import java.util.UUID.randomUUID
import java.util.stream.Stream
import kotlin.reflect.KClass
import kotlin.reflect.KFunction

internal class ReverseRouterTest {

    @ParameterizedTest
    @MethodSource("routeClassTests")
    fun `Given controller, when mapping route reversed, path should match expected`(
        clazz: KClass<*>,
        expected: String
    ) {
        route(clazz) shouldBeEqualTo expected
    }

    @ParameterizedTest
    @MethodSource("routeFunctionTests")
    fun `Given controller with function, when mapping route reversed, path should match expected`(
        clazz: KClass<*>,
        function: KFunction<*>,
        expected: String
    ) {
        route(clazz, function) shouldBeEqualTo expected
    }

    @ParameterizedTest
    @MethodSource("routeQueryComponentTests")
    fun `Given controller with a query component, when mapping route reversed, path should match expected`(
        clazz: KClass<*>,
        function: KFunction<*>,
        entity: BaseModel,
        expected: String
    ) {
        queryComponent(clazz, function, entity) shouldBeEqualTo expected
    }

    @Test
    fun `Given controller with no query component, when query component mapping route reversed, route should throw IllegalStateException`() {
        assertThrows<IllegalStateException> {
            queryComponent(UnderTest::class, UnderTest::requestEndpointIndexMapping, EntityOne())
        }
    }

    @Test
    fun `Given controller with invalid query component, when query component mapping route reversed, route should throw IllegalStateException`() {
        val unsupportedEntity = EntityTwo()
        assertThrows<IllegalStateException> {
            queryComponent(UnderTest::class, UnderTest::getEndpointSingleParameter, unsupportedEntity)
        }
    }

    companion object {
        @JvmStatic
        fun routeClassTests(): Stream<Arguments?>? {
            return Stream.of(
                arguments(UnderTest::class, "underTest"),
                arguments(UnderTestIndexMapping::class, ""),
            )
        }

        @JvmStatic
        fun routeFunctionTests(): Stream<Arguments?>? {
            return Stream.of(
                arguments(
                    UnderTest::class,
                    UnderTest::requestEndpointIndexMapping,
                    "underTest"
                ),
                arguments(
                    UnderTest::class,
                    UnderTest::requestEndpointSubMapping,
                    "underTest/subMapping"
                ),
                arguments(
                    UnderTest::class,
                    UnderTest::getEndpointSingleParameter,
                    "underTest/getEndpointSingleParameter"
                ),
                arguments(
                    UnderTest::class,
                    UnderTest::putEndpointMultiParameter,
                    "underTest/putEndpointMultiParameter"
                ),
            )
        }

        @JvmStatic
        fun routeQueryComponentTests(): Stream<Arguments?>? {
            val entityOne = EntityOne()
            val entityTwo = EntityTwo()
            val entityThree = EntityThree()

            return Stream.of(
                arguments(
                    UnderTest::class,
                    UnderTest::getEndpointSingleParameter,
                    entityOne,
                    "underTest/getEndpointSingleParameter?entityOne=${entityOne.id}"
                ),
                arguments(
                    UnderTest::class,
                    UnderTest::putEndpointMultiParameter,
                    entityTwo,
                    "underTest/putEndpointMultiParameter?entityTwo=${entityTwo.id}"
                ),
                arguments(
                    UnderTest::class,
                    UnderTest::postEndpointMixedParameter,
                    entityOne,
                    "underTest/postEndpointMixedParameter?entityOne=${entityOne.id}"
                ),
                arguments(
                    UnderTest::class,
                    UnderTest::deleteEndpointIndexMapping,
                    entityThree,
                    "underTest?entityThree=${entityThree.id}"
                ),
            )
        }
    }

    internal class EntityOne(override val id: UUID = randomUUID()) : BaseModel
    internal class EntityTwo(override val id: UUID = randomUUID()) : BaseModel
    internal class EntityThree(override val id: UUID = randomUUID()) : BaseModel

    @RequestMapping
    internal class UnderTestIndexMapping

    @RequestMapping("api/underTest")
    internal class UnderTest {
        @RequestMapping
        fun requestEndpointIndexMapping() {
        }

        @RequestMapping("subMapping")
        fun requestEndpointSubMapping() {
        }

        @GetMapping("getEndpointSingleParameter")
        fun getEndpointSingleParameter(
            @QueryComponent(EntityOne::class) @RequestParam(name = "entityOne", required = false) entityOne: UUID?,
        ) {
        }

        @PutMapping("putEndpointMultiParameter")
        fun putEndpointMultiParameter(
            @QueryComponent(EntityOne::class) @RequestParam(name = "entityOne", required = false) entityOne: UUID?,
            @QueryComponent(EntityTwo::class) @RequestParam(name = "entityTwo", required = false) entityTwo: UUID?,
            @QueryComponent(EntityThree::class) @RequestParam(name = "entityThree", required = false) entityThree: UUID?,
        ) {
        }

        @PostMapping("postEndpointMixedParameter")
        fun postEndpointMixedParameter(
            @QueryComponent(EntityOne::class) @RequestParam(name = "entityOne", required = false) entityOne: UUID?,
            @QueryComponent(EntityTwo::class) @RequestParam(name = "entityTwo", required = false) entityTwo: UUID?,
            @RequestParam(name = "normalRequestParam", required = false) normalRequestParam: UUID?,
        ) {
        }

        @DeleteMapping
        fun deleteEndpointIndexMapping(
            @QueryComponent(EntityOne::class) @RequestParam(name = "entityOne", required = false) entityOne: UUID?,
            @QueryComponent(EntityTwo::class) @RequestParam(name = "entityTwo", required = false) entityTwo: UUID?,
            @QueryComponent(EntityThree::class) @RequestParam(name = "entityThree", required = false) entityThree: UUID?,
        ) {
        }
    }
}
