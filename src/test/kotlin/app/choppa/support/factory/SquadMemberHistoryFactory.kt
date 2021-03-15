package app.choppa.support.factory

import app.choppa.domain.member.Member
import app.choppa.domain.squad.Squad
import app.choppa.domain.squad.history.RevisionType
import app.choppa.domain.squad.history.RevisionType.*
import app.choppa.domain.squad.history.SquadMemberHistory
import java.time.Instant
import java.time.Instant.now

class SquadMemberHistoryFactory {
    companion object {

        /**
         * Creates a random Squad to Member History record.
         */
        fun create(
            squad: Squad = SquadFactory.create(),
            member: Member = MemberFactory.create(),
            revisionNumber: Int = (0..999).random(),
            revisionType: RevisionType = values()[(values().indices).random()],
            createDate: Instant = now(),
        ): SquadMemberHistory = SquadMemberHistory(squad, member, revisionNumber, revisionType, createDate)

        /**
         * Creates multiple Squad to Member History records.
         */
        fun create(
            amount: Int,
            squad: Squad = SquadFactory.create(),
            member: Member = MemberFactory.create(),
            startingRevisionNumber: Int = 0,
        ): List<SquadMemberHistory> =
            (startingRevisionNumber until amount + startingRevisionNumber).map() { currentRevisionNumber ->
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
