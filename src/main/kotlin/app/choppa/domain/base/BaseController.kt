package app.choppa.domain.base

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
        fun location(path: String, vararg uriVariableValues: Any): URI =
            fromCurrentRequest().path("/$path").buildAndExpand(*uriVariableValues).toUri()
    }

    @GetMapping(ID_PATH)
    fun get(@PathVariable id: UUID) = baseService
        .find(id)
        .run { ok().body(this) }

    @PutMapping(ID_PATH)
    fun put(@PathVariable id: UUID, @RequestBody updatedEntity: T): ResponseEntity<T> = baseService
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
        .save(newEntity)
        .run { created(location(ID_PATH, this.id)).build() }
}
