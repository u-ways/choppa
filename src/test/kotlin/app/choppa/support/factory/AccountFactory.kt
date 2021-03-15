package app.choppa.support.factory

import app.choppa.domain.account.Account
import app.choppa.support.base.Universe
import java.time.Instant
import java.util.*
import java.util.UUID.randomUUID

class AccountFactory {
    companion object : Universe() {
        /**
         * Creates a random chapter.
         */
        fun create(
            id: UUID = randomUUID(),
            provider: String = "PRO-$id".substring(0, 5),
            providerId: String = randomUUID().toString(),
            organisationName: String = "ORG-$providerId".substring(0, 15),
            createDate: Instant = Instant.now(),
            name: String = " ",
            profilePicture: String = " ",
            firstLogin: Boolean = false,
        ): Account = Account(
            id,
            provider,
            providerId,
            organisationName,
            createDate,
            name,
            profilePicture,
            firstLogin,
        )

        /**
         * Creates X amount of chapters with mutual attributes
         */
        fun create(
            amount: Int,
            sharedProvider: String = randomUUID().toString(),
            sharedOrganisationName: String = "ORG-$sharedProvider".substring(0, 15),
            areFirstLogin: Boolean = false,
        ): MutableList<Account> = (0 until amount).map {
            create(
                provider = sharedProvider,
                organisationName = sharedOrganisationName,
                firstLogin = areFirstLogin,
            )
        }.toMutableList()
    }
}
