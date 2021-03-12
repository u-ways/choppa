package app.choppa.domain.account

import app.choppa.domain.base.BaseController.Companion.location
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.created
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/accounts")
class AccountController(
    @Autowired private val accountService: AccountService,
) {
    @GetMapping("me")
    fun getLoggedInAccount(): ResponseEntity<Account> = ok()
        .body(accountService.resolveFromAuth())

    @GetMapping("demo")
    fun createDemoAuthorisation(): ResponseEntity<HttpStatus> = ok().apply {
        accountService.createDemoAuthorisation()
    }.build()

    @PostMapping
    fun createNewAccount(@RequestBody account: Account): ResponseEntity<Account> = created(location("me")).apply {
        accountService.createNewAccount(account)
    }.build()
}
