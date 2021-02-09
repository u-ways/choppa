package app.choppa.domain.account

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/accounts")
class AccountController() {
    @GetMapping("me")
    fun listChapters(loggedInUser: Account): Account {
        return loggedInUser;
    }
}
