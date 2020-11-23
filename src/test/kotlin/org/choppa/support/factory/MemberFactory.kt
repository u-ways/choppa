package org.choppa.support.factory

import org.choppa.domain.chapter.Chapter
import org.choppa.domain.chapter.Chapter.Companion.UNASSIGNED_ROLE
import org.choppa.domain.member.Member

class MemberFactory {
    @Suppress("MemberVisibilityCanBePrivate")
    companion object {
        /**
         * Create a random member.
         *
         * @param chapter Chapter the member's role. (default = UNASSIGNED_ROLE)
         * @return Member
         */
        fun create(chapter: Chapter = UNASSIGNED_ROLE): Member = Member(chapter = chapter)

        /**
         * Create X amount of members that can have a mutual or non-mutual roles
         *
         * @param amount Int the number of members to create.
         * @param sharedChapter Chapter the members shared role. (default = UNASSIGNED_ROLE)
         * @return MutableList<Member>
         */
        fun create(amount: Int, sharedChapter: Chapter = UNASSIGNED_ROLE): MutableList<Member> {
            return (0 until amount).map { this.create(sharedChapter) }.toMutableList()
        }
    }
}
