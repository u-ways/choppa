package app.choppa.config

import app.choppa.domain.account.AccountService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.Authentication
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.security.web.authentication.HttpStatusEntryPoint
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@EnableWebSecurity
class SecurityConfiguration(
    @Autowired private val accountService: AccountService,
) : WebSecurityConfigurerAdapter() {

    companion object {
        /**
         * All Endpoints that should require a valid session.
         */
        val AUTH_ENDPOINTS = arrayOf(
            "/api/**",
        )

        /**
         * Endpoints that do not require a valid session. By default endpoints are permitted
         * so only include endpoints that are covered in AUTH_ENDPOINTS but should not be.
         */
        val NO_AUTH_ENDPOINTS = arrayOf(
            "/api/accounts/demo"
        )
    }

    val successHandler = object : AuthenticationSuccessHandler {
        @Value("\${choppa.auth.success-redirect-location-prefix:}")
        private var successfulRedirectLocation: String = ""

        override fun onAuthenticationSuccess(
            request: HttpServletRequest?,
            response: HttpServletResponse?,
            authentication: Authentication?,
        ) {
            val endpoint = if (accountService.resolveFromAuth().firstLogin) "welcome" else "dashboard"
            response!!.sendRedirect("$successfulRedirectLocation/$endpoint")
        }
    }

    override fun configure(http: HttpSecurity) {
        http
            .authorizeRequests().antMatchers(*NO_AUTH_ENDPOINTS).permitAll()
            .and().authorizeRequests().antMatchers(*AUTH_ENDPOINTS).authenticated()
            .and().authorizeRequests().anyRequest().permitAll()
            .and().oauth2Login().loginPage("/login")
            .and().oauth2Login().successHandler(successHandler)
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
