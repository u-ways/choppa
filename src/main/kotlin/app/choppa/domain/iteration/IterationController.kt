package app.choppa.domain.iteration

import app.choppa.domain.account.Account
import app.choppa.domain.base.BaseController
import app.choppa.domain.base.BaseController.Companion.API_PREFIX
import app.choppa.domain.member.Member
import app.choppa.domain.squad.Squad
import app.choppa.domain.tribe.Tribe
import app.choppa.utils.QueryComponent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.*
import org.springframework.web.bind.annotation.*
import java.util.*

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
        account: Account
    ): ResponseEntity<List<Iteration>> = ok().body(iterationService.find(account))

    @PutMapping
    fun putCollection(
        @RequestBody updatedCollection: List<Iteration>,
        account: Account
    ): ResponseEntity<List<Iteration>> = iterationService
        .find(updatedCollection.map { it.id }, account)
        .also { iterationService.save(updatedCollection, account) }
        .run { created(location()).build() }

    @DeleteMapping
    fun deleteCollection(
        @RequestBody toDeleteCollection: List<Iteration>,
        account: Account
    ): ResponseEntity<List<Iteration>> = iterationService
        .find(toDeleteCollection.map { it.id }, account)
        .also { iterationService.delete(toDeleteCollection, account) }
        .run { noContent().build() }

    @PostMapping
    fun postCollection(
        @RequestBody newCollection: List<Iteration>,
        account: Account
    ): ResponseEntity<List<Iteration>> = iterationService
        .save(newCollection, account)
        .run { created(location()).build() }
}
