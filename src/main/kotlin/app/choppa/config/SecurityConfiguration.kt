package app.choppa.config

import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter

@EnableWebSecurity
class SecurityConfiguration : WebSecurityConfigurerAdapter() {

    companion object {
        /**
         * All Endpoints that should require a valid session.
         */
        val AUTH_ENDPOINTS = arrayOf(
            "/api/**",
        )
    }

    override fun configure(http: HttpSecurity) {
        http
            .authorizeRequests().antMatchers(*AUTH_ENDPOINTS).authenticated()
            .and().authorizeRequests().anyRequest().permitAll()
            .and().oauth2ResourceServer().jwt()
    }
}
