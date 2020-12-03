package app.choppa.domain.base

import java.util.UUID

interface BaseService<T : BaseModel> {
    fun find(id: UUID): T
    fun save(entity: T): T
    fun delete(entity: T): T

    fun <E> List<E>.orElseThrow(exception: () -> Nothing): List<E> = when {
        this.isEmpty() -> exception.invoke()
        else -> this
    }
}
