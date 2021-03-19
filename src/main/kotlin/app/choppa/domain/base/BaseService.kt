package app.choppa.domain.base

import app.choppa.domain.account.AccountService
import app.choppa.exception.AuthorizationException
import org.springframework.data.domain.Sort
import org.springframework.data.domain.Sort.Direction.ASC
import org.springframework.data.domain.Sort.by
import java.util.*

abstract class BaseService<T : BaseModel> (
    private val accountService: AccountService
) {
    abstract fun find(sort: Sort = by(ASC, BaseModel::name.name)): List<T>
    abstract fun find(id: UUID, sort: Sort = by(ASC, BaseModel::name.name)): T
    abstract fun find(ids: List<UUID>, sort: Sort = by(ASC, BaseModel::name.name)): List<T>
    abstract fun save(entity: T): T
    abstract fun save(entities: List<T>): List<T>
    abstract fun delete(entity: T): T
    abstract fun delete(entities: List<T>): List<T>

    fun <E> List<E>.orElseThrow(exception: () -> Nothing): List<E> = when {
        this.isEmpty() -> exception.invoke()
        else -> this
    }

    fun <E : BaseModel> List<E>.ownedByAuthenticated(): List<E> = this.run {
        val loggedInUser = accountService.resolveFromAuth()
        filter { it.account == loggedInUser }
    }

    fun <E : BaseModel> E.verifyAuthenticatedOwnership(): E = this.run {
        val loggedInUser = accountService.resolveFromAuth()
        if (account.id == loggedInUser.id) this
        else throw AuthorizationException("Entity with id [$id] does not belong to [${loggedInUser.id}].")
    }
}
