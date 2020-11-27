package org.choppa.utils

import kotlin.reflect.KClass

/**
 * An annotation that represents the parameter acts as part of a "query component".
 * It should be paired with @RequestParam inside a resource endpoint such as a method annotated with a @GetMapping
 * inside of a class that is annotated with @RequestMapping.
 * By annotating query components with this annotation you can programmatically get the url of the request param by using the ReverseRouter utility.
 *
 * @param type Represents the type that the query param represents
 *
 * @see org.choppa.utils.ReverseRouter
 * @see org.springframework.web.bind.annotation.RequestMapping
 */
@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class QueryComponent(
    val type: KClass<*>,
)
