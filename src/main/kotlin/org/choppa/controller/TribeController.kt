package org.choppa.controller

import org.choppa.controller.BaseController.Companion.ID_PATH
import org.choppa.controller.BaseController.Companion.location
import org.choppa.model.tribe.Tribe
import org.choppa.service.TribeService
import org.springframework.beans.factory.annotation.Autowired
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
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("api/tribes")
class TribeController(
    @Autowired private val tribeService: TribeService
) {

    @GetMapping
    fun listTribes(): ResponseEntity<List<Tribe>> =
        ok().body(tribeService.find())

    @GetMapping(ID_PATH)
    fun getTribe(@PathVariable id: UUID): ResponseEntity<Tribe> =
        ok().body(tribeService.find(id))

    @PutMapping(ID_PATH)
    fun updateTribe(@PathVariable id: UUID, @RequestBody updatedTribe: Tribe): ResponseEntity<Tribe> {
        tribeService.find(id)
        tribeService.save(updatedTribe)
        return created(location(ID_PATH, id)).build()
    }

    @DeleteMapping(ID_PATH)
    fun deleteTribe(@PathVariable id: UUID): ResponseEntity<Tribe> {
        val tribe = tribeService.find(id)
        tribeService.delete(tribe)
        return noContent().build()
    }

    @PostMapping
    fun postTribe(@RequestBody newTribe: Tribe): ResponseEntity<Tribe> {
        val tribe = tribeService.save(newTribe)
        return created(location(ID_PATH, tribe.id)).build()
    }
}
