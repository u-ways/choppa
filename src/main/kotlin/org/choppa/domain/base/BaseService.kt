package org.choppa.domain.base

abstract class BaseService {
    internal fun <E> List<E>.orElseThrow(exception: () -> Nothing): List<E> = when {
        this.isEmpty() -> exception.invoke()
        else -> this
    }
}
