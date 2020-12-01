package app.choppa.domain.base

import java.util.UUID

interface BaseService<T : BaseModel> {
    fun find(): List<T>
    fun find(id: UUID): T
    fun find(ids: List<UUID>): List<T>
    fun save(entity: T): T
    fun save(entities: List<T>): List<T>
    fun delete(entity: T): T
    fun delete(entities: List<T>): List<T>

    fun <E> List<E>.orElseThrow(exception: () -> Nothing): List<E> = when {
        this.isEmpty() -> exception.invoke()
        else -> this
    }
}
