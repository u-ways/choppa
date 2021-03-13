package app.choppa.integration.domain.squad

import app.choppa.domain.account.Account
import app.choppa.domain.account.AccountService
import app.choppa.domain.member.Member
import app.choppa.domain.member.MemberService
import app.choppa.domain.squad.Squad
import app.choppa.domain.squad.SquadService
import app.choppa.domain.squad.history.SquadMemberHistoryService
import app.choppa.exception.EntityNotFoundException
import app.choppa.support.flyway.FlywayMigrationConfig
import app.choppa.support.testcontainers.TestDBContainer
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import javax.transaction.Transactional

@SpringBootTest
@Testcontainers
@Import(FlywayMigrationConfig::class)
@ActiveProfiles("test")
internal class SquadServiceIT @Autowired constructor(
    private val squadService: SquadService,
    private val memberService: MemberService,
    private val squadMemberHistoryService: SquadMemberHistoryService,
) {
    @Container
    private val testDBContainer: TestDBContainer = TestDBContainer.get()

    @MockkBean(relaxed = true)
    private lateinit var accountService: AccountService

    private lateinit var entity: Squad

    @BeforeEach
    internal fun setUp() {
        every { accountService.resolveFromAuth() } returns Account.UNASSIGNED_ACCOUNT
        entity = squadService.save(Squad())
    }

    @Test
    @Transactional
    fun `Given new entity, when service saves new entity, then service should return same entity with generated id`() {
        val result = squadService.save(entity)

        result.id shouldBe entity.id
        result.name shouldBe entity.name
    }

    @Test
    @Transactional
    fun `Given existing entity in db, when service finds entity by id, then service should return correct entity`() {
        val existingEntity = entity
        val result = squadService.find(existingEntity.id)

        result.id shouldBe existingEntity.id
        result.name shouldBe existingEntity.name
    }

    @Test
    @Transactional
    fun `Given existing entity in db, when service deletes entity, then service should removes entity from db`() {
        val existingEntity = squadService.save(Squad())
        val removedEntity = squadService.delete(existingEntity)

        assertThrows(EntityNotFoundException::class.java) { squadService.find(removedEntity.id) }
    }

    @Test
    @Transactional
    fun `Given existing entity in db with related records, when service deletes entity, then service should removes entity and related records from db`() {
        val relatedMember = memberService.save(Member())
        val existingEntity = squadService.save(Squad(members = mutableListOf(relatedMember)))

        memberService.findRelatedBySquad(existingEntity.id).first() shouldBeEqualTo relatedMember
        assert(squadMemberHistoryService.findBySquad(existingEntity).isNotEmpty())

        val removedEntity = squadService.delete(existingEntity)

        assertThrows(EntityNotFoundException::class.java) { memberService.findRelatedBySquad(removedEntity.id) }
        assert(squadMemberHistoryService.findBySquad(removedEntity).isEmpty())
    }

    @AfterEach
    internal fun tearDown() {
        squadService.delete(entity)
    }
}
