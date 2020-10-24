package org.choppa.controller

import org.choppa.model.Member
import org.choppa.service.MemberService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/domain/members")
class MemberController(
    @Autowired private val memberService: MemberService
) {

    @GetMapping()
    fun getAllMembers(): List<Member> {
        return memberService.find()
    }
}
