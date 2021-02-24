package app.choppa.domain.squad

import app.choppa.domain.account.Account
import app.choppa.domain.base.BaseController
import app.choppa.domain.base.BaseController.Companion.API_PREFIX
import app.choppa.domain.member.Member
import app.choppa.domain.tribe.Tribe
import app.choppa.utils.QueryComponent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.*
import org.springframework.web.bind.annotation.*
import java.io.Serializable
import java.util.*

@RestController
@RequestMapping("$API_PREFIX/squads")
class SquadController(
    @Autowired private val squadService: SquadService,
) : BaseController<Squad>(squadService) {
    @GetMapping
    fun listSquads(
        @QueryComponent(Member::class) @RequestParam(name = "member", required = false) memberId: UUID?,
        @QueryComponent(Tribe::class) @RequestParam(name = "tribe", required = false) tribeId: UUID?,
        account: Account,
    ): ResponseEntity<List<Squad>> = ok().body(
        when {
            memberId is UUID -> squadService.findRelatedByMember(memberId, account)
            tribeId is UUID -> squadService.findRelatedByTribe(tribeId, account)
            else -> squadService.find(account)
        }
    )

    @PutMapping
    fun putCollection(
        @RequestBody updatedCollection: List<Squad>,
        account: Account
    ): ResponseEntity<List<Squad>> = squadService
        .find(updatedCollection.map { it.id }, account)
        .also { squadService.save(updatedCollection, account) }
        .run { created(location()).build() }

    @DeleteMapping
    fun deleteCollection(
        @RequestBody toDeleteCollection: List<Squad>,
        account: Account
    ): ResponseEntity<List<Squad>> = squadService
        .find(toDeleteCollection.map { it.id }, account)
        .also { squadService.delete(toDeleteCollection, account) }
        .run { noContent().build() }

    @PostMapping
    fun postCollection(
        @RequestBody newCollection: List<Squad>,
        account: Account,
    ): ResponseEntity<List<Squad>> = squadService
        .save(newCollection, account)
        .run { created(location()).build() }

    @GetMapping("stats")
    fun getStatistics(account: Account): ResponseEntity<HashMap<String, Serializable>> = ok().body(squadService.statistics(account))
}
