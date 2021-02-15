package app.choppa.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.security.oauth2.core.user.DefaultOAuth2User
import org.springframework.security.oauth2.core.user.OAuth2User

@Configuration
class DemoAccountConfiguration {
    companion object {
        /**
         * The name of the demo account
         */
        const val DEMO_ACCOUNT_NAME = "Choppa Demo"
    }

    @Bean
    fun demoAccount(): OAuth2AuthenticationToken {
        val attributes = mapOf("name" to DEMO_ACCOUNT_NAME)
        val authorities = listOf(SimpleGrantedAuthority("ROLE_USER"))
        val user: OAuth2User = DefaultOAuth2User(authorities, attributes, "name")
        return OAuth2AuthenticationToken(user, authorities, "choppa")
    }
}
