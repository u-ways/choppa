package app.choppa.integration.domain.squad

import app.choppa.domain.member.Member
import app.choppa.domain.member.MemberService
import app.choppa.domain.squad.SquadService
import app.choppa.support.base.BaseServiceIT
import app.choppa.support.factory.MemberFactory
import app.choppa.support.factory.SquadFactory
import app.choppa.support.matchers.containsInAnyOrder
import com.natpryce.hamkrest.assertion.assertThat
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import javax.transaction.Transactional

internal class SquadServiceNestedMemberOperationsIT @Autowired constructor(
    private val squadService: SquadService,
    private val memberService: MemberService,
) : BaseServiceIT() {

    @Test
    @Transactional
    fun `Given new squad with non-existent members, when squad service saves new squad, then squad service should also handle saving the non-existing members`() {
        // Create a new squad with "SquadCurrentMembers" members that do not exist in the members repository.
        val newSquad = SquadFactory.create(members = MemberFactory.create(2))
        val result = squadService.save(newSquad)

        assertThat(result.members, List<Member>::containsInAnyOrder, newSquad.members)
    }

    @Test
    @Transactional
    fun `Given new squad with existing members, when squad service saves new squad with one new member, then squad service should save the new member and add it to the list of squad current members`() {
        val newSquad = SquadFactory.create(members = memberService.save(MemberFactory.create(2)).toMutableList())
        val nonExistentMember = MemberFactory.create()

        squadService.save(
            newSquad.apply {
                this.members.add(nonExistentMember)
            }
        )

        val result = memberService.find(nonExistentMember.id)

        result shouldBeEqualTo nonExistentMember
    }
}
