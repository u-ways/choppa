package app.choppa.domain.account

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/accounts")
class AccountController(
    @Autowired private val demoAccount: OAuth2AuthenticationToken,
    @Autowired private val accountService: AccountService,
) {

    @GetMapping("me")
    fun getLoggedInAccount(loggedInUser: Account): ResponseEntity<Account> = ok()
        .body(loggedInUser)

    @GetMapping("/demo")
    fun getDemoToken(): ResponseEntity<HttpStatus> {
        SecurityContextHolder.getContext().authentication = demoAccount
        return ok().build()
    }

    @PostMapping
    fun createAccount(@RequestBody account: Account): ResponseEntity<Account> {
        accountService.createAccount(account)
        return ok().build()
    }
}
