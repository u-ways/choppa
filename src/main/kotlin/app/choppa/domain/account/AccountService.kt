package app.choppa.domain.account

import app.choppa.domain.account.AccountDemoSeed.Companion.DEMO_NAME
import app.choppa.domain.account.AccountDemoSeed.Companion.DEMO_ORGANISATION_NAME
import app.choppa.domain.account.AccountDemoSeed.Companion.DEMO_PROVIDER
import app.choppa.domain.account.AccountDemoSeed.Companion.DEMO_TOKEN_LIMIT
import app.choppa.exception.UnsupportedProviderException
import app.choppa.utils.OAuth2TokenBuilder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder.getContext
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant.now
import java.util.UUID.randomUUID

@Service
class AccountService(
    @Autowired private val accountRepository: AccountRepository
) {
    fun find(provider: String) = accountRepository.findByProviderOrderByCreateDate(provider)

    fun save(account: Account) = accountRepository.save(account)

    fun delete(account: Account) = accountRepository.delete(account)

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
                ?: Account(randomUUID(), provider, providerId, "", now(), name, profilePicture, true)
        }
    }

    @Transactional
    fun createDemoAccount() = run {
        find(DEMO_PROVIDER).let {
            if (it.size > DEMO_TOKEN_LIMIT) delete(it.first())
        }
        save(
            Account(
                provider = DEMO_PROVIDER,
                providerId = "${randomUUID()}",
                organisationName = DEMO_ORGANISATION_NAME,
                firstLogin = false
            )
        ).apply {
            getContext().authentication = OAuth2TokenBuilder()
                .withName(DEMO_PROVIDER)
                .withRegistrationId(provider)
                .withAttribute("sub", providerId)
                .withAttribute("name", DEMO_NAME)
                .withAuthority(SimpleGrantedAuthority("ROLE_USER"))
                .build()
        }
    }
}
