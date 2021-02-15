package app.choppa.domain.account.converter

import org.springframework.security.oauth2.core.user.OAuth2User

interface OAuth2UserToAccountConverter {
    fun uniqueIdentifier(oauth2User: OAuth2User): String

    fun name(oauth2User: OAuth2User): String {
        return oauth2User.getAttribute<String>("name")!!
    }
}
