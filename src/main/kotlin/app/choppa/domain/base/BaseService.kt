package app.choppa.domain.base

import app.choppa.domain.account.Account
import app.choppa.exception.AuthorizationException
import java.util.*

interface BaseService<T : BaseModel> {
    fun find(account: Account): List<T>
    fun find(id: UUID, account: Account): T
    fun find(ids: List<UUID>, account: Account): List<T>
    fun save(entity: T, account: Account): T
    fun save(entities: List<T>, account: Account): List<T>
    fun delete(entity: T, account: Account): T
    fun delete(entities: List<T>, account: Account): List<T>

    fun <E> List<E>.orElseThrow(exception: () -> Nothing): List<E> = when {
        this.isEmpty() -> exception.invoke()
        else -> this
    }

    fun <E : BaseModel> List<E>.ownedBy(account: Account): List<E> = this
        .filter { it.account == account }

    fun <E : BaseModel> E.verifyOwnership(account: Account): E =
        if (this.account == account) this
        else throw AuthorizationException("Entity with id [${this.id}] does not belong to [${account.organisationName}].")
}
