package app.choppa.domain.member

import app.choppa.domain.account.Account
import app.choppa.domain.account.Account.Companion.UNASSIGNED_ACCOUNT
import app.choppa.domain.chapter.Chapter
import app.choppa.domain.chapter.Chapter.Companion.UNASSIGNED_ROLE
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*
import java.util.UUID.randomUUID

@Service
class MemberFactory(
    @Autowired private val memberService: MemberService,
) {
    fun create(
        id: UUID = randomUUID(),
        name: String = "ME-$id".substring(0, 15),
        chapter: Chapter = UNASSIGNED_ROLE,
        active: Boolean = true,
        account: Account = UNASSIGNED_ACCOUNT
    ): Member =
        memberService.save(Member(id, name, chapter, active), account)

    fun create(
        members: List<String>,
        sharedChapter: Chapter = UNASSIGNED_ROLE,
        sharedAccount: Account = UNASSIGNED_ACCOUNT,
        active: Boolean = true,
    ): MutableList<Member> = members
        .map { name -> create(name = name, chapter = sharedChapter, active = active, account = sharedAccount) }
        .toMutableList()
}
