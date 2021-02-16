package app.choppa.domain.account.provider

class GithubOAuth2Provider : OAuth2Provider {
    override fun uniqueIdentifier(oauth2User: org.springframework.security.oauth2.core.user.OAuth2User): String = oauth2User.getAttribute<Int>("id")!!.toString()
}
