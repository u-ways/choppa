package org.choppa.utils

import org.choppa.domain.base.BaseController.Companion.API_PREFIX
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter

class ReverseRouter {
    companion object {
        private val routeCache: ConcurrentHashMap<KClass<*>, String> = ConcurrentHashMap()

        fun route(clazz: KClass<*>): String = when {
            routeCache.contains(clazz) -> routeCache[clazz]!!
            else -> getRequestMappingValue(clazz.annotations).apply { routeCache[clazz] = this }
        }

        fun route(clazz: KClass<*>, function: KFunction<*>): String = route(clazz).let {
            val method = getRequestMappingValue(function.annotations)
            if (method.isNotEmpty()) "$it/$method" else it
        }

        fun route(clazz: KClass<*>, function: KFunction<*>, type: KClass<*>): String {
            val requestParam = function.parameters.extractParameterBy(type).ifEmpty {
                throw IllegalStateException("Expected a function parameter with @QueryComponent type: [${type.simpleName}]")
            }.last() as RequestParam

            val className = type.simpleName!!.decapitalize()
            val relatedEntity = requestParam.name

            check(relatedEntity == className) { "Expected @RequestParam name to be [$className]" }

            return "${route(clazz, function)}?$relatedEntity"
        }

        fun route(clazz: KClass<*>, id: Any) =
            "${route(clazz)}/$id"

        fun route(clazz: KClass<*>, function: KFunction<*>, id: Any) =
            "${route(clazz, function)}/$id"

        fun route(clazz: KClass<*>, function: KFunction<*>, type: KClass<*>, id: Any) =
            "${route(clazz, function, type)}=$id"

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

        private fun List<KParameter>.extractParameterBy(type: KClass<*>): List<Annotation> = this
            .drop(1)
            .map { parameter -> parameter.annotations }
            .first { annotations -> annotations.hasAMatching(type) }

        private fun List<Annotation>.hasAMatching(type: KClass<*>): Boolean {
            return this.any { it is QueryComponent && it.type == type }
        }
    }
}
