package app.choppa.utils

import app.choppa.domain.base.BaseController.Companion.API_PREFIX
import app.choppa.domain.base.BaseModel
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter

data class ReverseRouteCacheElement(
    private val clazz: KClass<*>,
    private val function: KFunction<*>? = null,
    private val type: KClass<out BaseModel>? = null,
)

@Component
class ReverseRouter(
    private val routeCache: ConcurrentHashMap<ReverseRouteCacheElement, String> = ConcurrentHashMap(),
) {

    /**
     * Generates a URI representing the class that has been passed in.
     * The class should be annotated with @RequestMapping or one of its aliases such as @GetMapping or @PostMapping
     *
     * If the class' RequestMapping starts with BaseController.API_PREFIX, the result will not contain this
     * For example a controller class annotated with @RequestMapping("myEndpoint") would return a path "myEndpoint"
     * While a class annotated with @RequestMapping("${BaseController.API_PREFIX}/myEndpoint") would also return the path "myEndpoint"
     *
     * @param clazz The class that is annotated with RequestMapping
     * @return A string containing the path that is set in the clazz's @RequestMapping
     */
    internal fun route(clazz: KClass<*>): String = when {
        routeCache.containsKey(ReverseRouteCacheElement(clazz)) -> routeCache[ReverseRouteCacheElement(clazz)]!!
        else -> routeAndAddToCache(clazz)
    }

    /**
     * Generates a URI representing the function that has been targeted
     * The function should be annotated with @RequestMapping or one of its aliases such as @GetMapping or @PostMapping
     * The class that the function is inside should also be annotated with @RequestMapping
     *
     * The notes notes for {@code route(KClass<*>)} also applies.
     *
     * For example a function annotated with @GetMapping("myMethod")
     * inside a class that is annotated with @RequestMapping("myEndpoint")
     * would return "myEndpoint/myMethod"
     *
     * @param clazz The class that is annotated with RequestMapping
     * @param function The method reference that is annotated with RequestingMapping or one of its aliases.
     * @return A string containing the path that is set in the clazz's @RequestMapping appended with the function's @RequestMapping value.
     */
    internal fun route(clazz: KClass<*>, function: KFunction<*>): String = when {
        routeCache.containsKey(ReverseRouteCacheElement(clazz, function)) -> routeCache[ReverseRouteCacheElement(clazz, function)]!!
        else -> routeAndAddToCache(clazz, function)
    }

    /**
     * Generates a URI representing the queryParam that has been targeted.
     * The type should represent the type inside the @QueryComponent that the request parameter is annotated with.
     * The function should be annotated with @RequestMapping or one of its aliases such as @GetMapping or @PostMapping
     * The class that the function is inside should also be annotated with @RequestMapping
     *
     * The @RequestParam value must match the @QueryType type class name with the initial character in lowercase.
     * For example The @QueryComponent(MyDataType::class) expects @RequestParam(name = "myDataType")
     *
     * The notes notes for {@code route(KClass<*>)} and {@code route(KClass<*>, KFunction<*>)} also applies.
     *
     * For example a function annotated with @GetMapping("myMethod")
     * that also has a parameter annotated with @QueryType(MyClass::class) @RequestParam("myClass")
     * inside a class that is annotated with @RequestMapping("myEndpoint")
     * would return "myEndpoint/myMethod?myClass"
     *
     * @param clazz The class that is annotated with RequestMapping
     * @param function The method reference that is annotated with RequestingMapping or one of its aliases.
     * @param type The type that matches what is inside @QueryComponent on the parameter.
     * @return A string containing the path that is set in the clazz's @RequestMapping appended with the function's @RequestMapping value
     * appended with the parameter's @RequestParam value
     */
    internal fun route(clazz: KClass<*>, function: KFunction<*>, type: KClass<out BaseModel>): String = when {
        routeCache.containsKey(ReverseRouteCacheElement(clazz, function, type)) -> routeCache[ReverseRouteCacheElement(clazz, function, type)]!!
        else -> routeAndAddToCache(clazz, function, type)
    }

    private fun routeAndAddToCache(clazz: KClass<*>): String =
        getRequestMappingValue(clazz.annotations).apply { routeCache[ReverseRouteCacheElement(clazz)] = this }

    private fun routeAndAddToCache(clazz: KClass<*>, function: KFunction<*>): String = route(clazz).let {
        val method = getRequestMappingValue(function.annotations)
        (if (method.isNotEmpty()) "$it/$method" else it).apply { routeCache[ReverseRouteCacheElement(clazz, function)] = this }
    }

    private fun routeAndAddToCache(clazz: KClass<*>, function: KFunction<*>, type: KClass<out BaseModel>): String {
        val requestParam = function.parameters
            .extractParameterBy(type)
            .find { it is RequestParam } as RequestParam

        val className = type.simpleName!!.decapitalize()
        val relatedEntity = requestParam.name

        check(relatedEntity == className) { "Expected @RequestParam name [$relatedEntity] to be [$className]" }

        return "${route(clazz, function)}?$relatedEntity".apply { routeCache[ReverseRouteCacheElement(clazz, function, type)] = this }
    }

    internal fun route(clazz: KClass<*>, id: UUID) =
        "${route(clazz)}/$id"

    internal fun route(clazz: KClass<*>, function: KFunction<*>, type: BaseModel) =
        "${route(clazz, function)}/${type.id}"

    internal fun queryComponent(clazz: KClass<*>, function: KFunction<*>, type: BaseModel) =
        "${route(clazz, function, type::class)}=${type.id}"

    private fun getRequestMappingValue(annotations: List<Annotation>): String = annotations.let {
        when (
            val annotation = it.find { annotation ->
                annotation is RequestMapping ||
                    annotation is GetMapping ||
                    annotation is PostMapping ||
                    annotation is PutMapping ||
                    annotation is DeleteMapping
            }
        ) {
            is RequestMapping -> annotation.value
            is GetMapping -> annotation.value
            is PostMapping -> annotation.value
            is PutMapping -> annotation.value
            is DeleteMapping -> annotation.value
            else -> throw IllegalStateException("No request mapping attribute found")
        }
    }
        .let { if (it.isNotEmpty()) it.first() else "" }
        .removePrefix("$API_PREFIX/")

    private fun List<KParameter>.extractParameterBy(type: KClass<*>): List<Annotation> = this.runCatching {
        this.drop(1)
            .map { parameter -> parameter.annotations }
            .first { annotations -> annotations.hasAMatching(type) }
            .apply {
                check(this.any { it is RequestParam })
            }
    }.getOrElse {
        throw IllegalStateException(
            "Expected to find two function parameters: " +
                "1. @QueryComponent with type: [${type.simpleName}] " +
                "2. @RequestParam with name [${type.simpleName!!.decapitalize()}]"
        )
    }

    private fun List<Annotation>.hasAMatching(type: KClass<*>): Boolean {
        return this.any { it is QueryComponent && it.type == type }
    }
}
