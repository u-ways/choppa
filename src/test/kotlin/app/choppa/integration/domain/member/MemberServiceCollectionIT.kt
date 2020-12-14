package app.choppa.integration.domain.member

import app.choppa.domain.member.Member
import app.choppa.domain.member.MemberService
import app.choppa.exception.EntityNotFoundException
import app.choppa.support.factory.MemberFactory
import app.choppa.support.flyway.FlywayMigrationConfig
import app.choppa.support.matchers.containsInAnyOrder
import app.choppa.support.testcontainers.TestDBContainer
import com.natpryce.hamkrest.assertion.assertThat
import org.junit.Assert.assertThrows
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
internal class MemberServiceCollectionIT @Autowired constructor(
    private val memberService: MemberService,
) {
    @Container
    private val testDBContainer: TestDBContainer = TestDBContainer.get()

    @Test
    @Transactional
    fun `Given a new list of members, when service saves said list of members, then service should persist the list of members`() {
        val newListOfMembers = MemberFactory.create(amount = 3)
        val result = memberService.save(newListOfMembers)

        assertThat(result, List<Member>::containsInAnyOrder, newListOfMembers)
    }

    @Test
    @Transactional
    fun `Given an existing list of members, when service updates said list of members, then service should persist the changed list of members`() {
        val existingListOfMembers = memberService.save(MemberFactory.create(amount = 3))
        val newName = "newName"

        val updatedListOfMembers = existingListOfMembers.map {
            Member(it.id, newName)
        }

        val result = memberService.save(updatedListOfMembers)

        assertThat(result, List<Member>::containsInAnyOrder, updatedListOfMembers)
    }

    @Test
    @Transactional
    fun `Given an existing list of members, when service deletes said list of members, then service should remove the existing list of members`() {
        val existingListOfMembers = memberService.save(MemberFactory.create(amount = 3))
        val removedListOfMembers = memberService.delete(existingListOfMembers)

        assertThrows(EntityNotFoundException::class.java) { memberService.find(removedListOfMembers.map { it.id }) }
    }
}
