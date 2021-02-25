package app.choppa.domain.account

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface AccountRepository : JpaRepository<Account, UUID> {
    fun findAccountByProviderAndProviderId(provider: String, providerId: String): Account
    fun existsAccountByProviderAndProviderId(provider: String, providerId: String): Boolean
}
