package app.choppa.domain.squad

import app.choppa.domain.base.BaseController
import app.choppa.domain.base.BaseController.Companion.API_PREFIX
import app.choppa.domain.member.Member
import app.choppa.domain.tribe.Tribe
import app.choppa.utils.QueryComponent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.*
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
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
) : BaseController<Squad>(squadService) {
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

    @PutMapping
    fun putCollection(@RequestBody updatedCollection: List<Squad>): ResponseEntity<List<Squad>> = squadService
        .find(updatedCollection.map { it.id })
        .also { squadService.save(updatedCollection) }
        .run { created(location()).build() }

    @DeleteMapping
    fun deleteCollection(@RequestBody toDeleteCollection: List<Squad>): ResponseEntity<List<Squad>> = squadService
        .find(toDeleteCollection.map { it.id })
        .also { squadService.delete(toDeleteCollection) }
        .run { noContent().build() }

    @PostMapping
    fun postCollection(@RequestBody newCollection: List<Squad>): ResponseEntity<List<Squad>> = squadService
        .save(newCollection)
        .run { created(location()).build() }
}
