package app.choppa.domain.account

import app.choppa.domain.account.Account.Companion.DEMO_ACCOUNT
import app.choppa.exception.UnsupportedProviderException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder.getContext
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.security.oauth2.core.user.DefaultOAuth2User
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service
import java.util.UUID.randomUUID

@Service
class AccountService(
    @Autowired private val accountRepository: AccountRepository,
) {
    fun save(account: Account): Account = accountRepository.save(account)

    fun createDemoAuthorisation() {
        val attributes = mapOf("name" to DEMO_ACCOUNT.name, "sub" to DEMO_ACCOUNT.providerId)
        val authorities = listOf(SimpleGrantedAuthority("ROLE_USER"))
        val user: OAuth2User = DefaultOAuth2User(authorities, attributes, "name")

        getContext().authentication = OAuth2AuthenticationToken(user, authorities, DEMO_ACCOUNT.provider)
    }

    fun createNewAccount(account: Account) = resolveFromAuth().run {
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
                ?: Account(randomUUID(), provider, providerId, "", name, profilePicture, true)
        }
    }
}
