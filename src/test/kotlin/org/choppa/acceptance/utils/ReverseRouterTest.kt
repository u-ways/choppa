package org.choppa.acceptance.utils

import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldContain
import org.choppa.domain.base.BaseModel
import org.choppa.utils.QueryComponent
import org.choppa.utils.ReverseRouteCacheElement
import org.choppa.utils.ReverseRouter
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.web.bind.annotation.*
import java.util.*
import java.util.UUID.randomUUID
import java.util.concurrent.ConcurrentHashMap
import java.util.stream.Stream
import kotlin.reflect.KClass
import kotlin.reflect.KFunction

internal class ReverseRouterTest {

    private lateinit var reverseRouter: ReverseRouter

    @BeforeEach
    internal fun setUp() {
        reverseRouter = ReverseRouter()
    }

    @ParameterizedTest
    @MethodSource("routeClassTests")
    fun `Given controller, when mapping route reversed, path should match expected`(
        clazz: KClass<*>,
        expected: String,
    ) {
        reverseRouter.route(clazz) shouldBeEqualTo expected
    }

    @ParameterizedTest
    @MethodSource("routeFunctionTests")
    fun `Given controller with function, when mapping route reversed, path should match expected`(
        clazz: KClass<*>,
        function: KFunction<*>,
        expected: String,
    ) {
        reverseRouter.route(clazz, function) shouldBeEqualTo expected
    }

    @ParameterizedTest
    @MethodSource("routeQueryComponentTests")
    fun `Given controller with a query component, when mapping route reversed, path should match expected`(
        clazz: KClass<*>,
        function: KFunction<*>,
        entity: BaseModel,
        expected: String,
    ) {
        reverseRouter.queryComponent(clazz, function, entity) shouldBeEqualTo expected
    }

    @Test
    fun `Given controller with no query component, when query component mapping route reversed, route should throw IllegalStateException`() {
        assertThrows<IllegalStateException> {
            reverseRouter.queryComponent(UnderTest::class, UnderTest::requestEndpointIndexMapping, EntityOne())
        }
    }

    @Test
    fun `Given controller with invalid query component, when query component mapping route reversed, route should throw IllegalStateException`() {
        val unsupportedEntity = EntityTwo()
        assertThrows<IllegalStateException> {
            reverseRouter.queryComponent(UnderTest::class, UnderTest::getEndpointSingleParameter, unsupportedEntity)
        }
    }

    @Test
    fun `Given controller which is not annotated as a RequestMapping, when mapping route reversed, route should throw IllegalStateException`() {
        assertThrows<IllegalStateException> {
            reverseRouter.route(UnderTestNoRequestMapping::class)
        }
    }

    @Test
    fun `Given controller with a non-request method, when mapping route reversed, route should throw IllegalStateException`() {
        assertThrows<IllegalStateException> {
            reverseRouter.route(UnderTest::class, UnderTest::noMapping)
        }
    }

    @Test
    fun `Given controller with a non-request method and a valid query component, when mapping route reversed, should throw IllegalStateException`() {
        assertThrows<IllegalStateException> {
            reverseRouter.route(UnderTest::class, UnderTest::noMappingValidQueryComponent, EntityOne())
        }
    }

    @Test
    fun `Given controller with a QueryComponent that is different to the RequestParam, when query component mapping route reversed, should throw IllegalStateException`() {
        assertThrows<IllegalStateException> {
            reverseRouter.queryComponent(UnderTest::class, UnderTest::getEndpointIncorrectRequestParamName, EntityOne())
        }.message shouldBeEqualTo "Expected @RequestParam name [invalidName] to be [entityOne]"
    }

    @Test
    fun `Given controller with a parameter annotated with QueryComponent but not RequestParam, when query component mapping route reversed, should throw IllegalStateException`() {
        assertThrows<IllegalStateException> {
            reverseRouter.queryComponent(UnderTest::class, UnderTest::getEndpointWithQueryComponentNoRequestParam, EntityOne())
        }.message shouldBeEqualTo "Expected to find two function parameters: 1. @QueryComponent with type: [EntityOne] 2. @RequestParam with name [entityOne]"
    }

    @Test
    fun `Given controller has query params in different order, when mapping route reversed, path should match expected`() {
        val entityOne = EntityOne()
        reverseRouter.queryComponent(
            UnderTest::class,
            UnderTest::getEndpointWithQueryComponentOrderedDifferently,
            entityOne
        ) shouldBeEqualTo "underTest?entityOne=${entityOne.id}"

        val entityTwo = EntityTwo()
        reverseRouter.queryComponent(
            UnderTest::class,
            UnderTest::getEndpointWithQueryComponentOrderedDifferently,
            entityTwo
        ) shouldBeEqualTo "underTest?entityTwo=${entityTwo.id}"
    }

    @Test
    fun `Given controller has not got a RequestParam annotation but the method does, when mapping route reversed, should throw IllegalStateException`() {
        assertThrows<IllegalStateException> {
            reverseRouter.route(UnderTestNoRequestMapping::class, UnderTestNoRequestMapping::getEndpointNoParameter)
        }
    }

    @Test
    fun `Given controller has not got a RequestParam annotation but the method does and has a valid query param, when query component mapping route reversed, should throw IllegalStateException`() {
        assertThrows<IllegalStateException> {
            reverseRouter.queryComponent(UnderTestNoRequestMapping::class, UnderTestNoRequestMapping::getEndpointNoParameter, EntityOne())
        }
    }

    @Test
    fun `Given controller, when mapping route reversed, the cache should be updated with the calculated path`() {
        val cache = ConcurrentHashMap<ReverseRouteCacheElement, String>()
        reverseRouter = ReverseRouter(cache)
        reverseRouter.route(UnderTest::class)

        cache shouldContain (ReverseRouteCacheElement(UnderTest::class) to "underTest")
    }

    @Test
    fun `Given controller and the reverse router has the path in the cache, when mapping route reversed, the result should be read from the cache`() {
        reverseRouter = ReverseRouter(
            ConcurrentHashMap(
                mapOf(
                    ReverseRouteCacheElement(UnderTest::class) to "ACachedResultOfAController"
                )
            )
        )

        reverseRouter.route(UnderTest::class) shouldBeEqualTo "ACachedResultOfAController"
    }

    @Test
    fun `Given controller and function, when mapping route reversed, the cache should be updated with the calculated path`() {
        val cache = ConcurrentHashMap<ReverseRouteCacheElement, String>()
        reverseRouter = ReverseRouter(cache)
        reverseRouter.route(UnderTest::class, UnderTest::requestEndpointSubMapping)

        cache shouldContain (ReverseRouteCacheElement(UnderTest::class, UnderTest::requestEndpointSubMapping) to "underTest/subMapping")
    }

    @Test
    fun `Given controller and function and the reverse router has the path in the cache, when mapping route reversed, the result should be read from the cache`() {
        reverseRouter = ReverseRouter(
            ConcurrentHashMap(
                mapOf(
                    ReverseRouteCacheElement(UnderTest::class, UnderTest::requestEndpointSubMapping) to "ACachedResultOfAControllerAndFunction"
                )
            )
        )

        reverseRouter.route(UnderTest::class, UnderTest::requestEndpointSubMapping) shouldBeEqualTo "ACachedResultOfAControllerAndFunction"
    }

    @Test
    fun `Given query param, when mapping route reversed, the cache should be updated with the calculated path`() {
        val cache = ConcurrentHashMap<ReverseRouteCacheElement, String>()
        reverseRouter = ReverseRouter(cache)
        reverseRouter.route(UnderTest::class, UnderTest::putEndpointMultiParameter, EntityOne::class)

        cache shouldContain (ReverseRouteCacheElement(UnderTest::class, UnderTest::putEndpointMultiParameter, EntityOne::class) to "underTest/putEndpointMultiParameter?entityOne")
    }

    @Test
    fun `Given query param and the reverse router has the path in the cache, when mapping route reversed, the result should be read from the cache`() {
        reverseRouter = ReverseRouter(
            ConcurrentHashMap(
                mapOf(
                    ReverseRouteCacheElement(UnderTest::class, UnderTest::requestEndpointSubMapping, EntityOne::class) to "ACachedResultOfAQueryParam"
                )
            )
        )

        reverseRouter.route(UnderTest::class, UnderTest::requestEndpointSubMapping, EntityOne::class) shouldBeEqualTo "ACachedResultOfAQueryParam"
    }

    @Suppress("unused")
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

        fun noMapping() {
        }

        fun noMappingValidQueryComponent(
            @QueryComponent(EntityOne::class) @RequestParam(name = "entityOne", required = false) entityOne: UUID?,
        ) {
        }

        @GetMapping
        fun getEndpointIncorrectRequestParamName(
            @QueryComponent(EntityOne::class) @RequestParam(name = "invalidName", required = false) entityOne: UUID?,
        ) {
        }

        @GetMapping
        fun getEndpointWithQueryComponentNoRequestParam(
            @QueryComponent(EntityOne::class) entityOne: UUID?,
        ) {
        }

        @GetMapping
        fun getEndpointWithQueryComponentOrderedDifferently(
            @QueryComponent(EntityOne::class) @RequestParam(name = "entityOne", required = false) entityOne: UUID?,
            @RequestParam(name = "entityTwo", required = false) @QueryComponent(EntityTwo::class) entityTwo: UUID?,
        ) {
        }
    }

    internal class UnderTestNoRequestMapping {
        @GetMapping("getEndpointNoParameter")
        fun getEndpointNoParameter() {
        }

        @GetMapping("getEndpointSingleParameter")
        fun getEndpointSingleParameter(
            @QueryComponent(EntityOne::class) @RequestParam(name = "entityOne", required = false) entityOne: UUID?,
        ) {
        }
    }
}
