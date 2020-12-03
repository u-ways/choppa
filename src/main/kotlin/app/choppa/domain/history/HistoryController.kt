package app.choppa.domain.history

import app.choppa.domain.base.BaseController.Companion.API_PREFIX
import app.choppa.domain.iteration.Iteration
import app.choppa.domain.member.Member
import app.choppa.domain.squad.Squad
import app.choppa.domain.tribe.Tribe
import app.choppa.utils.QueryComponent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.Instant.ofEpochMilli
import java.util.UUID

@RestController
@RequestMapping("$API_PREFIX/history")
class HistoryController(
    @Autowired private val historyService: HistoryService,
) {
    // FUTURE(u-ways) #124 Beyond MVP, we can provide endpoints to get the history
    //                     before or after a given date and/or provide mixed query strings.
    //                     For example, we can provide a mixed query string to get all iterations before
    //                     a given timestamp. (i.e. `.../history?iteration=<UUID>&before=<TIMESTAMP>`)
    @GetMapping
    fun listHistory(
        @QueryComponent(Iteration::class) @RequestParam(name = "iteration", required = false) iterationId: UUID?,
        @QueryComponent(Tribe::class) @RequestParam(name = "tribe", required = false) tribeId: UUID?,
        @QueryComponent(Squad::class) @RequestParam(name = "squad", required = false) squadId: UUID?,
        @QueryComponent(Member::class) @RequestParam(name = "member", required = false) memberId: UUID?,
        @RequestParam(name = "before", required = false) before: Long?,
        @RequestParam(name = "after", required = false) after: Long?,
    ): ResponseEntity<List<History>> = ok().body(
        when {
            iterationId is UUID -> historyService.findRelatedByIteration(iterationId)
            tribeId is UUID -> historyService.findRelatedByTribe(tribeId)
            squadId is UUID -> historyService.findRelatedBySquad(squadId)
            memberId is UUID -> historyService.findRelatedByMember(memberId)
            before is Long -> historyService.findAllByCreateDateBefore(ofEpochMilli(before))
            after is Long -> historyService.findAllByCreateDateAfter(ofEpochMilli(after))
            else -> historyService.find()
        }
    )
}
