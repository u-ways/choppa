package app.choppa.config.security

import app.choppa.domain.account.AccountService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class SuccessHandler(
    @Autowired private val accountService: AccountService,
) : AuthenticationSuccessHandler {

    @Value("\${choppa.auth.success-redirect-location-prefix:}")
    private var successfulRedirectLocation: String = ""

    override fun onAuthenticationSuccess(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        authentication: Authentication?,
    ) {
        val account = accountService.convert(authentication!!)
        if (accountService.isFirstTimeLogin(account)) {
            response!!.sendRedirect("$successfulRedirectLocation/welcome")
        } else {
            response!!.sendRedirect("$successfulRedirectLocation/dashboard")
        }
    }
}
