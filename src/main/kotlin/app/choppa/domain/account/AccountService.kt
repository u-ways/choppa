package app.choppa.domain.account

import app.choppa.domain.account.converter.NoOAuth2UserToAccountConverterFoundException
import app.choppa.domain.account.converter.impl.ChoppaOAuth2UserToAccountConverter
import app.choppa.domain.account.converter.impl.GithubOAuth2UserToAccountConverter
import app.choppa.domain.account.converter.impl.OpenIdOAuth2UserToAccountConverter
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
            "choppa" to ChoppaOAuth2UserToAccountConverter(),
            "github" to GithubOAuth2UserToAccountConverter(),
            "microsoft" to OpenIdOAuth2UserToAccountConverter(),
            "google" to OpenIdOAuth2UserToAccountConverter(),
            "okta" to OpenIdOAuth2UserToAccountConverter(),
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
