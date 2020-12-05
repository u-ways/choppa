package app.choppa.domain.iteration

import app.choppa.domain.base.BaseController
import app.choppa.domain.base.BaseController.Companion.API_PREFIX
import app.choppa.domain.member.Member
import app.choppa.domain.squad.Squad
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
@RequestMapping("$API_PREFIX/iterations")
class IterationController(
    @Autowired private val iterationService: IterationService,
) : BaseController<Iteration>(iterationService) {
    @GetMapping
    fun listIterations(
        @QueryComponent(Member::class) @RequestParam(name = "member", required = false) memberId: UUID?,
        @QueryComponent(Squad::class) @RequestParam(name = "squad", required = false) squadId: UUID?,
        @QueryComponent(Tribe::class) @RequestParam(name = "tribe", required = false) tribeId: UUID?,
    ): ResponseEntity<List<Iteration>> = ok().body(
        when {
            tribeId is UUID -> iterationService.findRelatedByTribe(tribeId)
            squadId is UUID -> iterationService.findRelatedBySquad(squadId)
            memberId is UUID -> iterationService.findRelatedByMember(memberId)
            else -> iterationService.find()
        }
    )

    @PutMapping
    fun putCollection(@RequestBody updatedCollection: List<Iteration>): ResponseEntity<List<Iteration>> = iterationService
        .find(updatedCollection.map { it.id })
        .also { iterationService.save(updatedCollection) }
        .run { created(location()).build() }

    @DeleteMapping
    fun deleteCollection(@RequestBody toDeleteCollection: List<Iteration>): ResponseEntity<List<Iteration>> = iterationService
        .find(toDeleteCollection.map { it.id })
        .also { iterationService.delete(toDeleteCollection) }
        .run { noContent().build() }

    @PostMapping
    fun postCollection(@RequestBody newCollection: List<Iteration>): ResponseEntity<List<Iteration>> = iterationService
        .save(newCollection)
        .run { created(location()).build() }
}
