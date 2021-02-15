package app.choppa.domain.account

import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface AccountRepository : JpaRepository<Account, UUID> {
    fun findAccountByProviderAndProviderId(provider: String, providerId: String): Account
    fun existsAccountByProviderAndProviderId(provider: String, providerId: String): Boolean
}
