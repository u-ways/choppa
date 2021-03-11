package app.choppa.integration.domain.member

import app.choppa.domain.account.Account.Companion.UNASSIGNED_ACCOUNT
import app.choppa.domain.chapter.Chapter
import app.choppa.domain.chapter.ChapterService
import app.choppa.domain.member.MemberFactory
import app.choppa.domain.member.MemberService
import app.choppa.support.flyway.FlywayMigrationConfig
import app.choppa.support.testcontainers.TestDBContainer
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldContainAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.util.UUID.randomUUID
import javax.transaction.Transactional

@SpringBootTest
@Testcontainers
@Import(FlywayMigrationConfig::class)
@ActiveProfiles("test")
internal class MemberFactoryIT @Autowired constructor(
    private val memberFactory: MemberFactory,
    private val memberService: MemberService,
    private val chapterService: ChapterService,
) {

    @Container
    private val testDBContainer: TestDBContainer = TestDBContainer.get()

    @Test
    @Transactional
    fun `Given new member details, when factory creates the member, it should store member in database`() {
        val id = randomUUID()
        val name = "Member Test"
        val chapter = chapterService.save(Chapter(), UNASSIGNED_ACCOUNT)
        val active = false

        memberFactory.create(id, name, chapter, active, UNASSIGNED_ACCOUNT)

        val expectedMember = memberService.find(id, UNASSIGNED_ACCOUNT)

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
        val shoredChapter = chapterService.save(Chapter(), UNASSIGNED_ACCOUNT)
        val sharedAccount = UNASSIGNED_ACCOUNT
        val active = true

        memberFactory.create(names, shoredChapter, sharedAccount, active)

        val expectedMembers = memberService
            .findRelatedByChapter(shoredChapter.id, sharedAccount)

        expectedMembers.size shouldBe names.size
        expectedMembers.map { it.name }.shouldContainAll(names)

        expectedMembers.random().chapter.id shouldBe shoredChapter.id
        expectedMembers.random().chapter.name shouldBe shoredChapter.name
        expectedMembers.random().active shouldBe active
    }
}
