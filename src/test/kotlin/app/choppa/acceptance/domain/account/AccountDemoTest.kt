package app.choppa.acceptance.domain.account

import app.choppa.domain.account.Account
import app.choppa.domain.account.AccountRepository
import app.choppa.domain.account.AccountService
import io.mockk.mockkClass
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken

internal class AccountDemoTest {
    private lateinit var securityContext: SecurityContext
    private lateinit var repository: AccountRepository
    private lateinit var service: AccountService

    @BeforeEach
    internal fun setUp() {
        securityContext = mockkClass(SecurityContext::class)
        repository = mockkClass(AccountRepository::class)
        service = AccountService(repository)
    }

    @Test
    fun `Given a demo authentication request, when service createDemoAuthorisation, then service set user session to Choppa token`() {
        service.createDemoAuthorisation()

        val token = SecurityContextHolder.getContext().authentication as OAuth2AuthenticationToken

        token.authorizedClientRegistrationId shouldBeEqualTo Account.DEMO_ACCOUNT.provider
        token.principal.attributes["name"] shouldBeEqualTo Account.DEMO_ACCOUNT.name
        token.principal.attributes["sub"] shouldBeEqualTo Account.DEMO_ACCOUNT.providerId
        token.principal.authorities.first() shouldBeEqualTo SimpleGrantedAuthority("ROLE_USER")
    }
}
