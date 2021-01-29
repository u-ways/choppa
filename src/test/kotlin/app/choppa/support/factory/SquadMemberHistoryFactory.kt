package app.choppa.support.factory

import app.choppa.domain.member.Member
import app.choppa.domain.squad.Squad
import app.choppa.domain.squad.history.RevisionType
import app.choppa.domain.squad.history.RevisionType.*
import app.choppa.domain.squad.history.SquadMemberHistory
import java.time.Instant
import java.time.Instant.now

class SquadMemberHistoryFactory {
    @Suppress("MemberVisibilityCanBePrivate")
    companion object {

        /**
         * Creates a random Squad Member History record.
         *
         * @param squad Squad the squad this history record belong to
         * @param member Member the member with the affected changes within the squad
         * @param revisionNumber Int the revision number
         * @param revisionType RevisionType the revision type (i.e. ADD revision)
         * @param createDate Instant the timestamp this revision occurred on.
         * @return SquadMemberHistory
         */
        fun create(
            squad: Squad = Squad(),
            member: Member = Member(),
            revisionNumber: Int = (0..999).random(),
            revisionType: RevisionType = values()[(values().indices).random()],
            createDate: Instant = now(),
        ): SquadMemberHistory = SquadMemberHistory(squad, member, revisionNumber, revisionType, createDate)

        /**
         * Creates a Squad's History records for specified member.
         *
         * @param amount Int the number of records to create
         * @param squad Squad the squad this history record belong to
         * @param member Member the member with the affected changes within the squad
         * @return SquadMemberHistory
         */
        fun create(
            amount: Int,
            squad: Squad = Squad(),
            member: Member = Member(),
            startingRevisionNumber: Int = 0,
        ): List<SquadMemberHistory> = (startingRevisionNumber until amount + startingRevisionNumber).map() { currentRevisionNumber ->
            create(
                squad,
                member,
                currentRevisionNumber,
                if (currentRevisionNumber % 2 == 0) ADD else REMOVE,
                now().plusSeconds(currentRevisionNumber.toLong())
            )
        }.sortedByDescending { it.revisionNumber }
    }
}
