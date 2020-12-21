package app.choppa.domain.tribe

import app.choppa.domain.base.BaseController
import app.choppa.domain.base.BaseController.Companion.API_PREFIX
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.created
import org.springframework.http.ResponseEntity.noContent
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("$API_PREFIX/tribes")
class TribeController(
    @Autowired private val tribeService: TribeService
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
}
