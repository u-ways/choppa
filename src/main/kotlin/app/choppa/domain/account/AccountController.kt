package app.choppa.domain.account

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/accounts")
class AccountController(
    @Autowired private val demoAccount: OAuth2AuthenticationToken,
) {

    @GetMapping("me")
    fun listChapters(loggedInUser: Account): ResponseEntity<Account> {
        return ok().body(loggedInUser)
    }

    @GetMapping("/demo")
    fun getDemoToken(): ResponseEntity<HttpStatus> {
        SecurityContextHolder.getContext().authentication = demoAccount
        return ok().build()
    }
}
