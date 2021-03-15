package app.choppa.support.factory

import app.choppa.domain.account.Account
import app.choppa.domain.chapter.Chapter
import app.choppa.domain.member.Member
import app.choppa.support.base.Universe
import java.util.*
import java.util.UUID.randomUUID

class MemberFactory {
    companion object : Universe() {
        /**
         * Creates a random member.
         */
        fun create(
            id: UUID = randomUUID(),
            name: String = "ME-$id".substring(0, 15),
            chapter: Chapter = CHAPTER,
            active: Boolean = true,
            account: Account = ACCOUNT,
        ): Member = Member(id, name, chapter, active, account)

        /**
         * Create X amount of members with mutual attributes
         */
        fun create(
            amount: Int,
            sharedChapter: Chapter = CHAPTER,
            sharedAccount: Account = ACCOUNT,
            sharedActive: Boolean = true
        ): MutableList<Member> = (0 until amount).map {
            create(chapter = sharedChapter, account = sharedAccount, active = sharedActive)
        }.toMutableList()
    }
}
