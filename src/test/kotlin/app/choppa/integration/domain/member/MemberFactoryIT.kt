package app.choppa.integration.domain.member

import app.choppa.domain.chapter.ChapterService
import app.choppa.domain.member.MemberFactory
import app.choppa.domain.member.MemberService
import app.choppa.support.base.BaseServiceIT
import app.choppa.support.factory.ChapterFactory
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldContainAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.util.UUID.randomUUID
import javax.transaction.Transactional

internal class MemberFactoryIT @Autowired constructor(
    private val memberFactory: MemberFactory,
    private val memberService: MemberService,
    private val chapterService: ChapterService,
) : BaseServiceIT() {
    @Test
    @Transactional
    fun `Given new member details, when factory creates the member, it should store member in database`() {
        val id = randomUUID()
        val name = "Member Test"
        val chapter = chapterService.save(ChapterFactory.create())
        val active = false

        memberFactory.create(id, name, chapter, active, ACCOUNT)

        val expectedMember = memberService.find(id)

        expectedMember.id shouldBe id
        expectedMember.name shouldBe name
        expectedMember.chapter.id shouldBe chapter.id
        expectedMember.chapter.name shouldBe chapter.name
        expectedMember.active shouldBe active
    }

    @Test
    @Transactional
    fun `Given several members with a common chapter and account, when factory creates the members, it should store members in database`() {
        val names = listOf("Alfa", "Bravo", "Charlie", "Delta", "Echo")
        val shoredChapter = chapterService.save(ChapterFactory.create())
        val sharedAccount = ACCOUNT
        val active = true

        memberFactory.create(names, shoredChapter, sharedAccount, active)

        val expectedMembers = memberService
            .findRelatedByChapter(shoredChapter.id)

        expectedMembers.size shouldBe names.size
        expectedMembers.map { it.name }.shouldContainAll(names)

        expectedMembers.random().chapter.id shouldBe shoredChapter.id
        expectedMembers.random().chapter.name shouldBe shoredChapter.name
        expectedMembers.random().active shouldBe active
    }
}
