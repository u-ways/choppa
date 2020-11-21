package org.choppa.domain.member

import org.choppa.domain.base.BaseController.Companion.ID_PATH
import org.choppa.domain.base.BaseController.Companion.location
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
@RequestMapping("api/members")
class MemberController(
    @Autowired private val memberService: MemberService
) {

    @GetMapping
    fun listMembers(): ResponseEntity<List<Member>> =
        ok().body(memberService.find())

    @GetMapping(ID_PATH)
    fun getMember(@PathVariable id: UUID): ResponseEntity<Member> =
        ok().body(memberService.find(id))

    @PutMapping(ID_PATH)
    fun updateMember(@PathVariable id: UUID, @RequestBody updatedMember: Member): ResponseEntity<Member> {
        memberService.find(id)
        memberService.save(updatedMember)
        return created(location(ID_PATH, id)).build()
    }

    @DeleteMapping(ID_PATH)
    fun deleteMember(@PathVariable id: UUID): ResponseEntity<Member> {
        val member = memberService.find(id)
        memberService.delete(member)
        return noContent().build()
    }

    @PostMapping
    fun postMember(@RequestBody newMember: Member): ResponseEntity<Member> {
        val member = memberService.save(newMember)
        return created(location(ID_PATH, member.id)).build()
    }
}
