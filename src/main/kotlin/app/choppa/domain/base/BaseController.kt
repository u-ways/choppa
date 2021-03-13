package app.choppa.domain.base

import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.*
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest
import java.net.URI
import java.util.*

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
    fun get(@PathVariable id: UUID) = baseService
        .find(id)
        .run { ok().body(this) }

    @PutMapping(ID_PATH)
    fun put(@PathVariable id: UUID, @RequestBody updatedEntity: T): ResponseEntity<T> = baseService
        .requireMatching(id, updatedEntity.id)
        .find(id)
        .also { baseService.save(updatedEntity) }
        .run { created(location(ID_PATH, id)).build() }

    @DeleteMapping(ID_PATH)
    fun delete(@PathVariable id: UUID): ResponseEntity<T> = baseService
        .find(id)
        .also { baseService.delete(it) }
        .run { noContent().build() }

    @PostMapping(ID_PATH)
    fun post(@PathVariable id: UUID, @RequestBody newEntity: T): ResponseEntity<T> = baseService
        .requireMatching(id, newEntity.id)
        .save(newEntity)
        .run { created(location(ID_PATH, this.id)).build() }

    private fun BaseService<T>.requireMatching(expected: UUID, actual: UUID): BaseService<T> = this.apply {
        require(expected == actual) { "URI endpoint with id [$expected] does not match response body entity id [$actual]" }
    }
}
