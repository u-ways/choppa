package app.choppa.domain.account

import app.choppa.domain.chapter.Chapter
import app.choppa.domain.chapter.ChapterService
import app.choppa.domain.member.Member
import app.choppa.domain.member.MemberFactory
import app.choppa.domain.squad.SquadFactory
import app.choppa.domain.squad.history.RevisionType.ADD
import app.choppa.domain.squad.history.RevisionType.REMOVE
import app.choppa.domain.squad.history.SquadMemberHistory
import app.choppa.domain.squad.history.SquadMemberHistoryService
import app.choppa.domain.tribe.Tribe
import app.choppa.domain.tribe.TribeService
import app.choppa.utils.Color.Companion.BLUE
import app.choppa.utils.Color.Companion.BROWN
import app.choppa.utils.Color.Companion.GREEN
import app.choppa.utils.Color.Companion.PURPLE
import app.choppa.utils.Color.Companion.RED
import app.choppa.utils.Color.Companion.YELLOW
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant.now
import java.time.temporal.ChronoUnit.DAYS

@Service
class AccountDemoSeed(
    @Autowired private val chapterService: ChapterService,
    @Autowired private val memberFactory: MemberFactory,
    @Autowired private val squadFactory: SquadFactory,
    @Autowired private val SquadMemberHistoryService: SquadMemberHistoryService,
    @Autowired private val tribeService: TribeService,
) {
    companion object {
        const val DEMO_PROVIDER = "choppa"
        const val DEMO_ORGANISATION_NAME = "Choppa Organisation (Demo)"
        const val DEMO_NAME = "Choppa (Demo)"
        const val DEMO_TOKEN_LIMIT = 30
    }

    @Transactional
    fun create(account: Account) {
        val tfl = tribeService.save(Tribe(name = "TFL", color = BLUE, account = account))

        val teamLead = chapterService.save(Chapter(name = "1. LEAD", color = RED, account = account))
        val businessAnalyst = chapterService.save(Chapter(name = "2. BA", color = YELLOW, account = account))
        val tester = chapterService.save(Chapter(name = "3. TESTER", color = GREEN, account = account))
        val float = chapterService.save(Chapter(name = "4. FLOAT", color = BROWN, account = account))
        val developer = chapterService.save(Chapter(name = "5. DEV", color = BLUE, account = account))
        val intern = chapterService.save(Chapter(name = "6. INTERN", color = PURPLE, account = account))

        val squads = squadFactory.create(
            squads = listOf(
                Triple(
                    "Metropolitan",
                    RED,
                    mutableListOf(
                        Member(name = "Stefan M.", chapter = teamLead, account = account),
                        Member(name = "Abraham C.", chapter = businessAnalyst, account = account),
                        Member(name = "Maryam P.", chapter = tester, account = account),
                        Member(name = "Jess C.", chapter = tester, account = account),
                        Member(name = "Lamar C.", chapter = developer, account = account),
                        Member(name = "Maxine C.", chapter = developer, account = account),
                        Member(name = "Ethel A.", chapter = developer, account = account),
                        Member(name = "Hattie P.", chapter = developer, account = account),
                        Member(name = "Ezra B.", chapter = intern, account = account),
                    )
                ),
                Triple(
                    "Circle",
                    YELLOW,
                    mutableListOf(
                        Member(name = "Eidde K.", chapter = teamLead, account = account),
                        Member(name = "Rose B.", chapter = businessAnalyst, account = account),
                        Member(name = "Gerald M.", chapter = float, account = account),
                        Member(name = "Monty H.", chapter = tester, account = account),
                        Member(name = "Zahraa L.", chapter = developer, account = account),
                        Member(name = "Shantelle S.", chapter = developer, account = account),
                        Member(name = "Bob M.", chapter = developer, account = account),
                        Member(name = "Logan E.", chapter = developer, account = account),
                        Member(name = "Pat P.", chapter = developer, account = account),
                    )
                ),
                Triple(
                    "District",
                    GREEN,
                    mutableListOf(
                        Member(name = "Sohieb S.", chapter = teamLead, account = account),
                        Member(name = "Liam X.", chapter = businessAnalyst, account = account),
                        Member(name = "Jack J.", chapter = tester, account = account),
                        Member(name = "Sleem G.", chapter = developer, account = account),
                        Member(name = "Aida A.", chapter = developer, account = account),
                        Member(name = "Nikita R.", chapter = developer, account = account),
                        Member(name = "Dominic Q.", chapter = developer, account = account),
                        Member(name = "Romany R.", chapter = intern, account = account),
                        Member(name = "Uzumaki N.", chapter = intern, account = account),
                    )
                )
            ),
            sharedTribe = tfl,
            sharedAccount = account,
        )

        memberFactory.create(
            members = listOf("Cayson B.", "Archibald H.", "Annabelle M.", "Neha C."),
            sharedChapter = developer,
            sharedAccount = account,
            active = false,
        )

        val squadA = squads[0]
        val squadB = squads[1]
        val squadC = squads[2]

        // History for squad A
        SquadMemberHistoryService.apply {
            save(findBySquad(squadA).map { it.copy(revisionNumber = 3) })
            save(
                listOf(

                    SquadMemberHistory(
                        squadA,
                        revisionNumber = 2,
                        member = squadB.members[6],
                        revisionType = REMOVE,
                        createDate = now().minus(7L, DAYS)
                    ),
                    SquadMemberHistory(
                        squadA,
                        revisionNumber = 2,
                        member = squadC.members[4],
                        revisionType = REMOVE,
                        createDate = now().minus(7L, DAYS)
                    ),

                    SquadMemberHistory(
                        squadA,
                        revisionNumber = 1,
                        member = squadB.members[6],
                        revisionType = ADD,
                        createDate = now().minus(14L, DAYS)
                    ),
                    SquadMemberHistory(
                        squadA,
                        revisionNumber = 1,
                        member = squadA.members[3],
                        revisionType = REMOVE,
                        createDate = now().minus(14L, DAYS)
                    ),
                    SquadMemberHistory(
                        squadA,
                        revisionNumber = 1,
                        member = squadC.members[4],
                        revisionType = ADD,
                        createDate = now().minus(14L, DAYS)
                    ),


                    SquadMemberHistory(
                        squadA,
                        revisionNumber = 0,
                        member = squadA.members[3],
                        revisionType = ADD,
                        createDate = now().minus(21L, DAYS)
                    ),
                )
            )
        }

        // History for squad B
        SquadMemberHistoryService.apply {
            save(findBySquad(squadB).map { it.copy(revisionNumber = 3) })
            save(
                listOf(
                    SquadMemberHistory(
                        squadB,
                        revisionNumber = 2,
                        member = squadA.members[6],
                        revisionType = REMOVE,
                        createDate = now().minus(7L, DAYS)
                    ),
                    SquadMemberHistory(
                        squadB,
                        revisionNumber = 2,
                        member = squadC.members[4],
                        revisionType = REMOVE,
                        createDate = now().minus(7L, DAYS)
                    ),

                    SquadMemberHistory(
                        squadB,
                        revisionNumber = 1,
                        member = squadA.members[6],
                        revisionType = ADD,
                        createDate = now().minus(14L, DAYS)
                    ),
                    SquadMemberHistory(
                        squadB,
                        revisionNumber = 1,
                        member = squadB.members[3],
                        revisionType = REMOVE,
                        createDate = now().minus(14L, DAYS)
                    ),
                    SquadMemberHistory(
                        squadB,
                        revisionNumber = 1,
                        member = squadC.members[4],
                        revisionType = ADD,
                        createDate = now().minus(14L, DAYS)
                    ),


                    SquadMemberHistory(
                        squadB,
                        revisionNumber = 0,
                        member = squadB.members[3],
                        revisionType = ADD,
                        createDate = now().minus(21L, DAYS)
                    ),
                )
            )

            // History for squad B
            SquadMemberHistoryService.apply {
                save(findBySquad(squadC).map { it.copy(revisionNumber = 3) })
                save(
                    listOf(
                        SquadMemberHistory(
                            squadC,
                            revisionNumber = 2,
                            member = squadA.members[6],
                            revisionType = REMOVE,
                            createDate = now().minus(7L, DAYS)
                        ),
                        SquadMemberHistory(
                            squadC,
                            revisionNumber = 2,
                            member = squadB.members[4],
                            revisionType = REMOVE,
                            createDate = now().minus(7L, DAYS)
                        ),

                        SquadMemberHistory(
                            squadC,
                            revisionNumber = 1,
                            member = squadA.members[6],
                            revisionType = ADD,
                            createDate = now().minus(14L, DAYS)
                        ),
                        SquadMemberHistory(
                            squadC,
                            revisionNumber = 1,
                            member = squadC.members[3],
                            revisionType = REMOVE,
                            createDate = now().minus(14L, DAYS)
                        ),
                        SquadMemberHistory(
                            squadC,
                            revisionNumber = 1,
                            member = squadB.members[4],
                            revisionType = ADD,
                            createDate = now().minus(14L, DAYS)
                        ),


                        SquadMemberHistory(
                            squadC,
                            revisionNumber = 0,
                            member = squadC.members[3],
                            revisionType = ADD,
                            createDate = now().minus(21L, DAYS)
                        ),
                    )
                )
            }
        }
    }
}
