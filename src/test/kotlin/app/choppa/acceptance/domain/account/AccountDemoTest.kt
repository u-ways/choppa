package app.choppa.acceptance.domain.account

import app.choppa.domain.account.AccountDemo
import app.choppa.domain.account.AccountService
import app.choppa.domain.member.MemberFactory
import app.choppa.domain.squad.SquadFactory
import org.springframework.security.core.context.SecurityContext

internal class AccountDemoTest {
    private lateinit var securityContext: SecurityContext
    private lateinit var accountService: AccountService
    private lateinit var memberFactory: MemberFactory
    private lateinit var squadFactory: SquadFactory
    private lateinit var accountDemo: AccountDemo
}
