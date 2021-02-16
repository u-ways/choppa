package app.choppa.domain.account.provider

class ChoppaOAuth2Provider : OAuth2Provider {
    override fun uniqueIdentifier(oauth2User: org.springframework.security.oauth2.core.user.OAuth2User): String = "choppa-demo-account"
}
