package app.choppa.config

import org.springframework.http.HttpStatus
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.authentication.HttpStatusEntryPoint
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler
import org.springframework.security.web.util.matcher.AntPathRequestMatcher

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
            .and().oauth2Login().loginPage("/login")
            .and().oauth2Login().defaultSuccessUrl("/dashboard")
            .and().csrf().disable().logout().invalidateHttpSession(true)
            .logoutUrl("/api/logout").logoutSuccessHandler(HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK))
            .and().exceptionHandling()
            .defaultAuthenticationEntryPointFor(getRestAuthenticationEntryPoint(), AntPathRequestMatcher("/**"))
            .and().oauth2ResourceServer().jwt()
    }

    private fun getRestAuthenticationEntryPoint(): AuthenticationEntryPoint {
        return HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)
    }
}
