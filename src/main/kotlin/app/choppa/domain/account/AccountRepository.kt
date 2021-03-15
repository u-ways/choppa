package app.choppa.domain.account

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface AccountRepository : JpaRepository<Account, UUID> {
    fun findByProviderAndProviderId(provider: String, providerId: String): Account?
    fun findByProviderOrderByCreateDate(provider: String): List<Account>
}
