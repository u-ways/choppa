package app.choppa.domain.member

import app.choppa.domain.account.Account
import app.choppa.domain.base.BaseController
import app.choppa.domain.base.BaseController.Companion.API_PREFIX
import app.choppa.domain.chapter.Chapter
import app.choppa.domain.squad.Squad
import app.choppa.domain.tribe.Tribe
import app.choppa.utils.QueryComponent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.*
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("$API_PREFIX/members")
class MemberController(
    @Autowired private val memberService: MemberService,
) : BaseController<Member>(memberService) {
    @GetMapping
    fun listMembers(
        @QueryComponent(Chapter::class) @RequestParam(name = "chapter", required = false) chapterId: UUID?,
        @QueryComponent(Squad::class) @RequestParam(name = "squad", required = false) squadId: UUID?,
        @QueryComponent(Tribe::class) @RequestParam(name = "tribe", required = false) tribeId: UUID?,
        @RequestParam(name = "active", required = false, defaultValue = "true") active: Boolean = true,
        account: Account
    ): ResponseEntity<List<Member>> = ok().body(
        when {
            chapterId is UUID -> memberService.findRelatedByChapter(chapterId, account)
            squadId is UUID -> memberService.findRelatedBySquad(squadId, account)
            tribeId is UUID -> memberService.findRelatedByTribe(tribeId, account)
            !active -> memberService.findInactive(account)
            else -> memberService.find(account)
        }
    )

    @PutMapping
    fun putCollection(
        @RequestBody updatedCollection: List<Member>,
        account: Account
    ): ResponseEntity<List<Member>> = memberService
        .find(updatedCollection.map { it.id }, account)
        .also { memberService.save(updatedCollection, account) }
        .run { created(location()).build() }

    @DeleteMapping
    fun deleteCollection(
        @RequestBody toDeleteCollection: List<Member>,
        account: Account
    ): ResponseEntity<List<Member>> = memberService
        .find(toDeleteCollection.map { it.id }, account)
        .also { memberService.delete(toDeleteCollection, account) }
        .run { noContent().build() }

    @PostMapping
    fun postCollection(
        @RequestBody newCollection: List<Member>,
        account: Account
    ): ResponseEntity<List<Member>> = memberService
        .save(newCollection, account)
        .run { created(location()).build() }

    @GetMapping("stats")
    fun getStatistics(account: Account): ResponseEntity<HashMap<String, Any>> = ok().body(memberService.statistics(account))
}
