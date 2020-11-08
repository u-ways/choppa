package org.choppa.controller

import org.choppa.controller.BaseController.Companion.ID_PATH
import org.choppa.controller.BaseController.Companion.location
import org.choppa.model.squad.Squad
import org.choppa.service.SquadService
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
@RequestMapping("api/squads")
class SquadController(
    @Autowired private val squadService: SquadService
) {

    @GetMapping
    fun listSquads(): ResponseEntity<List<Squad>> =
        ok().body(squadService.find())

    @GetMapping(ID_PATH)
    fun getSquad(@PathVariable id: UUID): ResponseEntity<Squad> =
        ok().body(squadService.find(id))

    @PutMapping(ID_PATH)
    fun updateSquad(@PathVariable id: UUID, @RequestBody updatedSquad: Squad): ResponseEntity<Squad> {
        squadService.find(id)
        squadService.save(updatedSquad)
        return created(location(ID_PATH, id)).build()
    }

    @DeleteMapping(ID_PATH)
    fun deleteSquad(@PathVariable id: UUID): ResponseEntity<Squad> {
        val squad = squadService.find(id)
        squadService.delete(squad)
        return noContent().build()
    }

    @PostMapping
    fun postSquad(@RequestBody newSquad: Squad): ResponseEntity<Squad> {
        val squad = squadService.save(newSquad)
        return created(location(ID_PATH, squad.id)).build()
    }
}
