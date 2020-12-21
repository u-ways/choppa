package app.choppa.domain.tribe

import app.choppa.domain.base.BaseController
import app.choppa.domain.base.BaseController.Companion.API_PREFIX
import app.choppa.domain.rotation.RotationOptions
import app.choppa.domain.rotation.RotationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.created
import org.springframework.http.ResponseEntity.noContent
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("$API_PREFIX/tribes")
class TribeController(
    @Autowired private val tribeService: TribeService,
    @Autowired private val rotationService: RotationService
) : BaseController<Tribe>(tribeService) {
    @GetMapping
    fun listTribes(): ResponseEntity<List<Tribe>> =
        ok().body(tribeService.find())

    @PutMapping
    fun putCollection(@RequestBody updatedCollection: List<Tribe>): ResponseEntity<List<Tribe>> = tribeService
        .find(updatedCollection.map { it.id })
        .also { tribeService.save(updatedCollection) }
        .run { created(location()).build() }

    @DeleteMapping
    fun deleteCollection(@RequestBody toDeleteCollection: List<Tribe>): ResponseEntity<List<Tribe>> = tribeService
        .find(toDeleteCollection.map { it.id })
        .also { tribeService.delete(toDeleteCollection) }
        .run { noContent().build() }

    @PostMapping
    fun postCollection(@RequestBody newCollection: List<Tribe>): ResponseEntity<List<Tribe>> = tribeService
        .save(newCollection)
        .run { created(location()).build() }

    @PostMapping("$ID_PATH:rotate")
    fun executeRotation(
        @PathVariable id: UUID,
        @RequestBody(required = false) options: RotationOptions?
    ): ResponseEntity<Tribe> =
        ok().body(
            rotationService.rotate(
                tribeService.find(id),
                options ?: RotationOptions.DEFAULT_OPTIONS
            )
        )
}
