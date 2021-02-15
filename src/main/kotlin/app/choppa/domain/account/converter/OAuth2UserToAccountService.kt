package app.choppa.domain.account.converter

import app.choppa.domain.account.Account
import app.choppa.domain.account.converter.impl.ChoppaOAuth2UserToAccountConverter
import app.choppa.domain.account.converter.impl.GithubOAuth2UserToAccountConverter
import app.choppa.domain.account.converter.impl.OpenIdOAuth2UserToAccountConverter
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service

@Service
class OAuth2UserToAccountService {
    companion object {
        val CONVERTERS = mapOf(
            "choppa" to ChoppaOAuth2UserToAccountConverter(),
            "github" to GithubOAuth2UserToAccountConverter(),
            "microsoft" to OpenIdOAuth2UserToAccountConverter(),
            "google" to OpenIdOAuth2UserToAccountConverter(),
            "okta" to OpenIdOAuth2UserToAccountConverter(),
        )
    }

    fun convert(provider: String, oauth2User: OAuth2User): Account {
        val converter = CONVERTERS[provider] ?: throw NoOAuth2UserToAccountConverterFoundException(provider)

        return Account(
            provider,
            converter.uniqueIdentifier(oauth2User),
            converter.name(oauth2User),
        )
    }
}
