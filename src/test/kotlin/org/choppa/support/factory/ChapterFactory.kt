package org.choppa.support.factory

import org.choppa.model.chapter.Chapter

class ChapterFactory {
    @Suppress("MemberVisibilityCanBePrivate")
    companion object {
        /**
         * Creates a random chapter.
         *
         * @return Chapter
         */
        fun create(): Chapter = Chapter()

        /**
         * Creates X amount of random chapters
         * @param amount Int the number of chapters to create.
         * @return MutableList<Chapter>
         */
        fun create(amount: Int): MutableList<Chapter> {
            return (0 until amount).map { this.create() }.toMutableList()
        }
    }
}
