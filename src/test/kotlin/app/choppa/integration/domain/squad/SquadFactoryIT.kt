package app.choppa.integration.domain.squad

import app.choppa.domain.member.MemberFactory
import app.choppa.domain.squad.SquadFactory
import app.choppa.domain.squad.SquadService
import app.choppa.domain.tribe.TribeService
import app.choppa.support.base.BaseServiceIT
import app.choppa.support.factory.TribeFactory
import app.choppa.utils.Color.Companion.toRGBAInt
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldContainAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.util.UUID.randomUUID
import javax.transaction.Transactional

internal class SquadFactoryIT @Autowired constructor(
    private val squadFactory: SquadFactory,
    private val memberFactory: MemberFactory,
    private val squadService: SquadService,
    private val tribeService: TribeService,
) : BaseServiceIT() {

    @Test
    @Transactional
    fun `Given new squad details, when factory creates the squad, it should store squad in database`() {
        val id = randomUUID()
        val name = "Squad Test"
        val color = "#FFFFFF".toRGBAInt()
        val members = memberFactory.create(
            List(3) { "MEM-$it" },
            sharedAccount = ACCOUNT,
            sharedChapter = CHAPTER
        )

        squadFactory.create(id, name, color, TRIBE, members, ACCOUNT)

        val expectedSquad = squadService.find(id)

        expectedSquad.id shouldBe id
        expectedSquad.name shouldBe name
        expectedSquad.color shouldBe color
        expectedSquad.members shouldContainAll members
    }

    @Test
    @Transactional
    fun `Given several squad details, when factory creates the squads, it should store squads in database`() {
        val squads = listOf(
            Triple(
                "Alfa",
                "#FF0000".toRGBAInt(),
                memberFactory.create(
                    List(3) { "MEM-$it" },
                    sharedChapter = CHAPTER,
                    sharedAccount = ACCOUNT
                )
            ),
            Triple(
                "Bravo",
                "#00FF00".toRGBAInt(),
                memberFactory.create(
                    List(1) { "Member" },
                    sharedChapter = CHAPTER,
                    sharedAccount = ACCOUNT
                )
            ),
            Triple(
                "Charlie",
                "#0000FF".toRGBAInt(),
                memberFactory.create(
                    emptyList(),
                    sharedChapter = CHAPTER,
                    sharedAccount = ACCOUNT
                )
            ),
        )
        val sharedTribe = tribeService.save(TribeFactory.create())
        val sharedAccount = ACCOUNT

        squadFactory.create(squads, sharedTribe, sharedAccount)

        val expectedSquads = squadService.findRelatedByTribe(sharedTribe.id)

        expectedSquads.size shouldBe squads.size

        expectedSquads.map { it.name } shouldContainAll squads.map { it.first }
        expectedSquads.map { it.color } shouldContainAll squads.map { it.second }
        expectedSquads.map { it.members } shouldContainAll squads.map { it.third }
    }
}
