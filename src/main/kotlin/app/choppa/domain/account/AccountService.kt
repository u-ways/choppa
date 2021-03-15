package app.choppa.domain.account

import app.choppa.exception.UnsupportedProviderException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder.getContext
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.stereotype.Service
import java.time.Instant.now
import java.util.UUID.randomUUID

@Service
class AccountService(
    @Autowired private val accountRepository: AccountRepository,
) {
    fun find(provider: String) = accountRepository.findByProviderOrderByCreateDate(provider)

    fun save(account: Account): Account = accountRepository.save(account)

    fun delete(account: Account) = accountRepository.delete(account)

    fun createNewAccount(account: Account) =
        resolveFromAuth()
            .run {
                check(firstLogin) { "An account already exists for the logged in user." }
                save(copy(organisationName = account.organisationName))
                    .copy(name = name, profilePicture = profilePicture, firstLogin = false)
            }

    fun resolveFromAuth(): Account = with(getContext().authentication as OAuth2AuthenticationToken) {
        principal.run {
            val provider = authorizedClientRegistrationId
            val providerId = "${attributes["sub"] ?: attributes["id"] ?: throw UnsupportedProviderException(provider)}"
            val profilePicture = "${attributes["avatar_url"] ?: attributes["picture"] ?: attributes["profile"] ?: ""}"
            val name = "${attributes["name"] ?: ""}"

            accountRepository.findByProviderAndProviderId(provider, providerId)
                ?.copy(name = name, profilePicture = profilePicture)
                ?: Account(randomUUID(), provider, providerId, "", now(), name, profilePicture, true)
        }
    }
}
