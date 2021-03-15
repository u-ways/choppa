package app.choppa.domain.member

import app.choppa.domain.account.Account
import app.choppa.domain.chapter.Chapter
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
        chapter: Chapter,
        active: Boolean = true,
        account: Account,
    ): Member =
        memberService.save(Member(id, name, chapter, active, account = account))

    fun create(
        members: List<String>,
        sharedChapter: Chapter,
        sharedAccount: Account,
        active: Boolean = true,
    ): MutableList<Member> = members
        .map { name -> create(name = name, chapter = sharedChapter, active = active, account = sharedAccount) }
        .toMutableList()
}
