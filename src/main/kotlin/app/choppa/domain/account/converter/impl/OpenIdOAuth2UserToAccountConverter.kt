package app.choppa.domain.account.converter.impl

import app.choppa.domain.account.converter.OAuth2UserToAccountConverter
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser
import org.springframework.security.oauth2.core.user.OAuth2User

class OpenIdOAuth2UserToAccountConverter : OAuth2UserToAccountConverter {
    override fun uniqueIdentifier(oauth2User: OAuth2User): String {
        return (oauth2User as DefaultOidcUser).idToken.tokenValue;
    }
}
