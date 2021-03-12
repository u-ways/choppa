package app.choppa.acceptance.domain.account

import app.choppa.domain.account.Account
import app.choppa.domain.account.AccountRepository
import app.choppa.domain.account.AccountService
import app.choppa.support.builder.OAuth2TokenBuilder
import io.mockk.every
import io.mockk.mockkClass
import io.mockk.verify
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import java.util.*

internal class AccountServiceTest {
    private lateinit var repository: AccountRepository
    private lateinit var service: AccountService

    @BeforeEach
    internal fun setUp() {
        repository = mockkClass(AccountRepository::class)
        service = AccountService(repository)
    }

    @Test
    fun `Given new entity, when service saves new entity, then service should save in repository and return the same entity`() {
        val entity = Account()

        every { repository.findById(entity.id) } returns Optional.empty()
        every { repository.save(entity) } returns entity

        val savedEntity = service.save(entity)

        savedEntity shouldBe entity

        verify(exactly = 1) { repository.save(entity) }
    }

    @Test
    fun `Given an existing account, when service tries to createNewAccount, then service should throw IllegalStateException`() {
        val securityContext = mockkClass(SecurityContext::class)
        val entity = Account(
            firstLogin = false,
            provider = "provider",
            providerId = UUID.randomUUID().toString()
        )

        every {
            repository.findByProviderAndProviderId(entity.provider, entity.providerId)
        } returns entity

        every { securityContext.authentication } returns OAuth2TokenBuilder()
            .withName("name")
            .withRegistrationId(entity.provider)
            .withAttribute("id", entity.providerId)
            .withAuthority(SimpleGrantedAuthority("ROLE_USER"))
            .build()

        SecurityContextHolder.setContext(securityContext)

        assertThrows(IllegalStateException::class.java) {
            service.createNewAccount(entity.copy(firstLogin = true))
        }
    }

    @Test
    fun `Given new account, when service creates a new account, then service should create the new account and set its first login to false`() {
        val securityContext = mockkClass(SecurityContext::class)
        val entity = Account(
            firstLogin = true,
            provider = "provider",
            providerId = UUID.randomUUID().toString()
        )

        every {
            repository.findByProviderAndProviderId(entity.provider, entity.providerId)
        } returns null

        every {
            repository.save(any())
        } returns entity.copy(organisationName = entity.organisationName, firstLogin = false)

        every { securityContext.authentication } returns OAuth2TokenBuilder()
            .withName("name")
            .withRegistrationId(entity.provider)
            .withAttribute("id", entity.providerId)
            .withAuthority(SimpleGrantedAuthority("ROLE_USER"))
            .build()

        SecurityContextHolder.setContext(securityContext)

        val newAccount = service.createNewAccount(entity)

        newAccount.firstLogin shouldBeEqualTo false
        newAccount.organisationName shouldBeEqualTo entity.organisationName

        verify(exactly = 1) { repository.save(any()) }
    }
}
