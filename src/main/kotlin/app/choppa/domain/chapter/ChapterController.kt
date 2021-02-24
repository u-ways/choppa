package app.choppa.domain.chapter

import app.choppa.domain.account.Account
import app.choppa.domain.base.BaseController
import app.choppa.domain.base.BaseController.Companion.API_PREFIX
import app.choppa.domain.squad.Squad
import app.choppa.domain.tribe.Tribe
import app.choppa.utils.QueryComponent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.*
import org.springframework.web.bind.annotation.*
import java.io.Serializable
import java.util.*

@RestController
@RequestMapping("$API_PREFIX/chapters")
class ChapterController(
    @Autowired private val chapterService: ChapterService
) : BaseController<Chapter>(chapterService) {
    @GetMapping
    fun listChapters(
        @QueryComponent(Squad::class) @RequestParam(name = "squad", required = false) squadId: UUID?,
        @QueryComponent(Tribe::class) @RequestParam(name = "tribe", required = false) tribeId: UUID?,
        account: Account
    ): ResponseEntity<List<Chapter>> = ok().body(
        when {
            squadId is UUID -> chapterService.findRelatedBySquad(squadId, account)
            tribeId is UUID -> chapterService.findRelatedByTribe(tribeId, account)
            else -> chapterService.find(account)
        }
    )

    @PutMapping
    fun putCollection(
        @RequestBody updatedCollection: List<Chapter>,
        account: Account
    ): ResponseEntity<List<Chapter>> = chapterService
        .find(updatedCollection.map { it.id }, account)
        .also { chapterService.save(updatedCollection, account) }
        .run { created(location()).build() }

    @DeleteMapping
    fun deleteCollection(
        @RequestBody toDeleteCollection: List<Chapter>,
        account: Account
    ): ResponseEntity<List<Chapter>> = chapterService
        .find(toDeleteCollection.map { it.id }, account)
        .also { chapterService.delete(toDeleteCollection, account) }
        .run { noContent().build() }

    @PostMapping
    fun postCollection(
        @RequestBody newCollection: List<Chapter>,
        account: Account
    ): ResponseEntity<List<Chapter>> = chapterService
        .save(newCollection, account)
        .run { created(location()).build() }

    @GetMapping("stats")
    fun getStatistics(
        account: Account
    ): ResponseEntity<Map<String, Serializable>> = ok().body(chapterService.statistics(account))
}
