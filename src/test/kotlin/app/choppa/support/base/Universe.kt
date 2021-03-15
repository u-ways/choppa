package app.choppa.support.base

import app.choppa.domain.account.Account
import app.choppa.domain.chapter.Chapter
import app.choppa.domain.member.Member
import app.choppa.domain.member.Member.Companion.NO_MEMBERS
import app.choppa.domain.squad.Squad
import app.choppa.domain.squad.Squad.Companion.NO_SQUADS
import app.choppa.domain.tribe.Tribe
import app.choppa.utils.Color.Companion.GREY
import java.time.Instant
import java.util.*

/**
 * Entity types used for testing purpose such as:
 * - Quick test priming
 * - Known variables for quicker debugging
 * - Helpful for e2e testing
 *
 * When you use entities form this universe you be rest
 * assured that they all belong to the same organisation (Account)
 *
 * These entities can also be safely used in integration tests as
 * test seeding has been done for our test database container.
 *
 * @see `db/seed/R__unassigned_entity_types.sql`
 */
@Suppress("PropertyName")
abstract class Universe {

    private val commonId = UUID
        .fromString("00000000-0000-0000-0000-000000000000")

    val ACCOUNT: Account
        get() = Account(
            id = commonId,
            provider = "account_provider",
            providerId = "account_provider_id",
            organisationName = "account_organisation_name",
            createDate = Instant.parse("2020-01-01T00:00:00.00Z"),
            name = "account_name",
            profilePicture = "account_profile_picture",
            firstLogin = false,
        )

    val TRIBE: Tribe
        get() = Tribe(
            id = commonId,
            name = "tribe_name",
            color = GREY,
            squads = NO_SQUADS,
            account = ACCOUNT
        )

    val SQUAD: Squad
        get() = Squad(
            id = commonId,
            name = "squad_name",
            color = GREY,
            tribe = TRIBE,
            members = NO_MEMBERS,
            account = ACCOUNT
        )

    val CHAPTER: Chapter
        get() = Chapter(
            id = commonId,
            name = "chapter_name",
            color = GREY,
            account = ACCOUNT
        )

    val MEMBER: Member
        get() = Member(
            id = commonId,
            name = "member_name",
            chapter = CHAPTER,
            active = true,
            account = ACCOUNT
        )
}
