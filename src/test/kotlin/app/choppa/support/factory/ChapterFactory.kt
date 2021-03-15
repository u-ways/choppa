package app.choppa.support.factory

import app.choppa.domain.account.Account
import app.choppa.domain.chapter.Chapter
import app.choppa.support.base.Universe
import app.choppa.utils.Color.Companion.GREY
import java.util.*
import java.util.UUID.randomUUID

class ChapterFactory {
    companion object : Universe() {
        /**
         * Creates a random chapter.
         */
        fun create(
            id: UUID = randomUUID(),
            name: String = "CH-$id".substring(0, 15),
            color: Int = GREY,
            account: Account = ACCOUNT,
        ): Chapter = Chapter(id, name, color, account)

        /**
         * Creates X amount of chapters with mutual attributes
         */
        fun create(
            amount: Int,
            sharedAccount: Account = ACCOUNT,
        ): MutableList<Chapter> = (0 until amount).map {
            create(account = sharedAccount)
        }.toMutableList()
    }
}
