package org.choppa.utils

import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.VALUE_PARAMETER
import kotlin.reflect.KClass

/**
 * A no argument decorator that is used to decorate classes eligible for noArg initialisation.
 * It is mainly used to solve constructor hell issues. (i.e. JPA constructor hell)
 */
@Target(VALUE_PARAMETER)
@Retention(RUNTIME)
annotation class QueryComponent(
    val type: KClass<*>
)
