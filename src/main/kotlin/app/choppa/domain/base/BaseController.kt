package app.choppa.domain.base

import app.choppa.domain.account.Account
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.created
import org.springframework.http.ResponseEntity.noContent
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest
import java.net.URI
import java.util.UUID

@RequestMapping
abstract class BaseController<T : BaseModel>(
    private val baseService: BaseService<T>
) {
    companion object {
        /**
         * Standard prefix for an API endpoint
         */
        const val API_PREFIX: String = "api"

        /**
         * Standard path for an identification of a singular resource.
         */
        const val ID_PATH: String = "{id}"

        /**
         * Returns the location of the current URI request with the values expanded.
         */
        fun location(path: String = "", vararg uriVariableValues: Any): URI =
            fromCurrentRequest().path(path.let { if (it.isBlank()) it else "/$path" })
                .buildAndExpand(*uriVariableValues).toUri()
    }

    @GetMapping(ID_PATH)
    fun get(@PathVariable id: UUID, account: Account) = baseService
        .find(id, account)
        .run { ok().body(this) }

    @PutMapping(ID_PATH)
    fun put(@PathVariable id: UUID, @RequestBody updatedEntity: T, account: Account): ResponseEntity<T> = baseService
        .requireMatching(id, updatedEntity.id)
        .find(id, account)
        .also { baseService.save(updatedEntity, account) }
        .run { created(location(ID_PATH, id)).build() }

    @DeleteMapping(ID_PATH)
    fun delete(@PathVariable id: UUID, account: Account): ResponseEntity<T> = baseService
        .find(id, account)
        .also { baseService.delete(it, account) }
        .run { noContent().build() }

    @PostMapping(ID_PATH)
    fun post(@PathVariable id: UUID, @RequestBody newEntity: T, account: Account): ResponseEntity<T> = baseService
        .requireMatching(id, newEntity.id)
        .save(newEntity, account)
        .run { created(location(ID_PATH, this.id, account)).build() }

    private fun BaseService<T>.requireMatching(expected: UUID, actual: UUID): BaseService<T> = this.apply {
        require(expected == actual) { "URI endpoint with id [$expected] does not match response body entity id [$actual]" }
    }
}
