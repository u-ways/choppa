package org.choppa.integration.service.relations

import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeNull
import org.choppa.model.Member
import org.choppa.model.Squad
import org.choppa.model.chapter.Chapter
import org.choppa.model.relations.SquadCurrentMembers
import org.choppa.model.relations.SquadCurrentMembersId
import org.choppa.repository.relations.SquadCurrentMembersRepository
import org.choppa.service.ChapterService
import org.choppa.service.MemberService
import org.choppa.service.SquadService
import org.choppa.service.relations.SquadCurrentMembersService
import org.choppa.support.flyway.FlywayMigrationConfig
import org.choppa.support.testcontainers.TestDBContainer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.util.UUID.randomUUID
import javax.transaction.Transactional

private val CHAPTER = Chapter(id = randomUUID(), name = "chapterName")
private val MEMBER = Member(id = randomUUID(), name = "memberName", chapter = CHAPTER)
private val SQUAD = Squad(id = randomUUID(), name = "squadName")

@SpringBootTest
@Testcontainers
@Import(FlywayMigrationConfig::class)
@ActiveProfiles("test")
internal class SquadCurrentMembersServiceIT @Autowired constructor(
    private val squadCurrentMembersRepository: SquadCurrentMembersRepository,
    private val chapterService: ChapterService,
    private val memberService: MemberService,
    private val squadService: SquadService,
    private val squadCurrentMembersService: SquadCurrentMembersService
) {
    @Container
    private val testDBContainer: TestDBContainer = TestDBContainer.get()

    @BeforeEach
    @Transactional
    internal fun setUp() {
        squadService.save(SQUAD)
        memberService.save(MEMBER)
    }

    @Test
    @Transactional
    fun `Given new entity, when service saves new entity, then service should return same entities with generated id`() {
        val entity = SquadCurrentMembers(SQUAD, MEMBER)
        val result = squadCurrentMembersService.save(entity)

        val updatedMember = memberService.find(MEMBER.id)
        val updatedSquad = squadService.find(SQUAD.id)

        result.member shouldBe updatedMember
        result.squad shouldBe updatedSquad
        result.rotationDate shouldBe entity.rotationDate
    }

    @Test
    @Transactional
    fun `Given existing entity in db, when service finds entity by id, then service should return correct entities`() {
        val existingEntity = squadCurrentMembersService.save(SquadCurrentMembers(SQUAD, MEMBER))
        val result = squadCurrentMembersService.find(SquadCurrentMembersId(SQUAD.id, MEMBER.id))

        result?.member shouldBe existingEntity.member
        result?.squad shouldBe existingEntity.squad
        result?.rotationDate shouldBe existingEntity.rotationDate
    }

    @Test
    @Transactional
    fun `Given existing entity in db, when service deletes entity, then service should removes entity from db`() {
        val existingEntity = squadCurrentMembersService.save(SquadCurrentMembers(SQUAD, MEMBER))
        val removedEntity = squadCurrentMembersService.delete(existingEntity)
        val result = squadCurrentMembersService.find(
            SquadCurrentMembersId(removedEntity.squad.id, removedEntity.member.id)
        )

        result?.shouldBeNull()
    }

    @AfterEach
    internal fun tearDown() {
        squadCurrentMembersRepository.deleteAll()
        squadService.delete(SQUAD)
        memberService.delete(MEMBER)
        chapterService.delete(CHAPTER)
    }
}
