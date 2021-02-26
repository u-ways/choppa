package app.choppa.acceptance.domain.account

import app.choppa.domain.account.Account
import app.choppa.domain.account.Account.Companion.DEMO_ACCOUNT
import app.choppa.domain.account.AccountRepository
import app.choppa.domain.account.AccountService
import io.mockk.every
import io.mockk.mockkClass
import org.amshove.kluent.shouldBe
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class AccountServiceTest {
    private lateinit var repository: AccountRepository
    private lateinit var service: AccountService

    @BeforeEach
    internal fun setUp() {
        repository = mockkClass(AccountRepository::class)
        service = AccountService(repository)
    }

    @Test
    fun `Given a DEMO account, when service creates a demo AuthenticationToken, then user should be granted simple authority under the CHOPPA provider`() {
        val entity = DEMO_ACCOUNT
        val result = service.demoAccount()

        result.name shouldBe entity.name
        result.authorizedClientRegistrationId shouldBe entity.provider
        result.principal.attributes["name"] shouldBe entity.name
        result.principal.authorities.first().authority shouldBe "ROLE_USER"
        result.isAuthenticated shouldBe true
    }

    @Test
    fun `Given a first login account, when service checks isFirstTimeLogin, then service should return true`() {
        val entity = Account(firstLogin = true)

        every {
            repository.existsAccountByProviderAndProviderId(entity.provider, entity.providerId)
        } returns false

        service.isFirstTimeLogin(entity) shouldBe true
    }

    @Test
    fun `Given an already existing account, when service tries to recreate the account, then service should throw an exception`() {
        val entity = Account(firstLogin = false)

        every {
            repository.existsAccountByProviderAndProviderId(entity.provider, entity.providerId)
        } returns true

        assertThrows(Exception::class.java) { service.createAccount(entity) }
    }
}
