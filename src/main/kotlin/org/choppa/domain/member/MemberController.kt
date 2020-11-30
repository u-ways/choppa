package org.choppa.domain.member

import org.choppa.domain.base.BaseController
import org.choppa.domain.base.BaseController.Companion.API_PREFIX
import org.choppa.domain.chapter.Chapter
import org.choppa.domain.squad.Squad
import org.choppa.domain.tribe.Tribe
import org.choppa.utils.QueryComponent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

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
    ): ResponseEntity<List<Member>> = ok().body(
        when {
            chapterId is UUID -> memberService.findRelatedByChapter(chapterId)
            squadId is UUID -> memberService.findRelatedBySquad(squadId)
            tribeId is UUID -> memberService.findRelatedByTribe(tribeId)
            else -> memberService.find()
        }
    )
}
