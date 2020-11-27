package org.choppa.utils

import org.choppa.domain.base.BaseController.Companion.API_PREFIX
import org.choppa.domain.base.BaseModel
import org.springframework.web.bind.annotation.*
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter

class ReverseRouter {
    companion object {
        private val routeCache: ConcurrentHashMap<KClass<*>, String> = ConcurrentHashMap()

        internal fun route(clazz: KClass<*>): String = when {
            routeCache.contains(clazz) -> routeCache[clazz]!!
            else -> getRequestMappingValue(clazz.annotations).apply { routeCache[clazz] = this }
        }

        internal fun route(clazz: KClass<*>, function: KFunction<*>): String = route(clazz).let {
            val method = getRequestMappingValue(function.annotations)
            if (method.isNotEmpty()) "$it/$method" else it
        }

        internal fun route(clazz: KClass<*>, function: KFunction<*>, type: KClass<out BaseModel>): String {
            val requestParam = function.parameters
                .extractParameterBy(type)
                .find { it is RequestParam } as RequestParam

            val className = type.simpleName!!.decapitalize()
            val relatedEntity = requestParam.name

            check(relatedEntity == className) { "Expected @RequestParam name [$relatedEntity] to be [$className]" }

            return "${route(clazz, function)}?$relatedEntity"
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
}
