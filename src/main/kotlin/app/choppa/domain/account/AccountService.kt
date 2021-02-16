package app.choppa.domain.account

import app.choppa.domain.account.provider.ChoppaOAuth2Provider
import app.choppa.domain.account.provider.GithubOAuth2Provider
import app.choppa.domain.account.provider.OpenIdOAuth2Provider
import app.choppa.exception.NoOAuth2UserToAccountConverterFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service

@Service
class AccountService(
    @Autowired private val accountRepository: AccountRepository,
) {

    companion object {
        val CONVERTERS = mapOf(
            "choppa" to ChoppaOAuth2Provider(),
            "github" to GithubOAuth2Provider(),
            "microsoft" to OpenIdOAuth2Provider(),
            "google" to OpenIdOAuth2Provider(),
            "okta" to OpenIdOAuth2Provider(),
        )
    }

    fun convert(authentication: Authentication): Account {
        return convert(
            (SecurityContextHolder.getContext().authentication as OAuth2AuthenticationToken).authorizedClientRegistrationId,
            SecurityContextHolder.getContext().authentication.principal as OAuth2User
        )
    }

    fun convert(provider: String, oauth2User: OAuth2User): Account {
        val converter = CONVERTERS[provider] ?: throw NoOAuth2UserToAccountConverterFoundException(provider)
        val providerId = converter.uniqueIdentifier(oauth2User)
        val name = converter.name(oauth2User)

        return if (accountRepository.existsAccountByProviderAndProviderId(provider, providerId)) {
            val savedAccount = accountRepository.findAccountByProviderAndProviderId(provider, providerId)
            savedAccount.name = name
            savedAccount
        } else {
            Account(provider, providerId, name)
        }
    }

    fun isFirstTimeLogin(account: Account): Boolean = !accountRepository
        .existsAccountByProviderAndProviderId(account.provider, account.providerId)

    fun createAccount(account: Account): Account {
        if (!isFirstTimeLogin(account)) {
            throw Exception("An account already exists for this provider and providerId.")
        }

        return accountRepository.save(account)
    }
}
