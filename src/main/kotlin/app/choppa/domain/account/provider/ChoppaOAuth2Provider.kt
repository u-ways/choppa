package app.choppa.domain.account.provider

import app.choppa.domain.account.Account
import org.springframework.security.oauth2.core.user.OAuth2User

class ChoppaOAuth2Provider : OAuth2Provider {
    override fun uniqueIdentifier(oauth2User: OAuth2User): String = Account.DEMO_ACCOUNT.providerId
}
