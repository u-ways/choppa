package org.choppa.domain.squad

import org.choppa.domain.base.BaseController.Companion.API_PREFIX
import org.choppa.domain.base.BaseController.Companion.ID_PATH
import org.choppa.domain.base.BaseController.Companion.location
import org.choppa.domain.member.Member
import org.choppa.domain.tribe.Tribe
import org.choppa.utils.QueryComponent
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
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("$API_PREFIX/squads")
class SquadController(
    @Autowired private val squadService: SquadService,
) {

    @GetMapping
    fun listSquads(
        @QueryComponent(Member::class) @RequestParam(name = "member", required = false) memberId: UUID?,
        @QueryComponent(Tribe::class) @RequestParam(name = "tribe", required = false) tribeId: UUID?,
    ): ResponseEntity<List<Squad>> = ok().body(
        when {
            memberId is UUID -> squadService.findRelatedByMember(memberId)
            tribeId is UUID -> squadService.findRelatedByTribe(tribeId)
            else -> squadService.find()
        }
    )

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
