package app.choppa.domain.account.provider

import org.springframework.security.oauth2.core.user.OAuth2User

interface OAuth2Provider {
    fun uniqueIdentifier(oauth2User: OAuth2User): String
    fun name(oauth2User: OAuth2User): String = oauth2User.getAttribute<String>("name")!!
}
