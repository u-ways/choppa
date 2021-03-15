package app.choppa.integration.domain.account

import app.choppa.domain.account.AccountService
import app.choppa.support.builder.OAuth2TokenBuilder
import app.choppa.support.builder.SecurityContextBuilder
import app.choppa.support.factory.AccountFactory
import app.choppa.support.flyway.FlywayMigrationConfig
import app.choppa.support.testcontainers.TestDBContainer
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldContain
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ActiveProfiles
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.util.UUID.randomUUID

@SpringBootTest
@Testcontainers
@Import(FlywayMigrationConfig::class)
@ActiveProfiles("test")
internal class AccountServiceIT @Autowired constructor(
    private val accountService: AccountService,
) {
    @Container
    private val testDBContainer: TestDBContainer = TestDBContainer.get()

    @Test
    @WithMockUser
    fun `Given an existing account, when service tries to createNewAccount, then service should throw IllegalStateException`() {
        val entity = accountService.save(
            AccountFactory.create(
                firstLogin = false,
                provider = "provider",
                providerId = randomUUID().toString()
            )
        )

        SecurityContextHolder.setContext(
            SecurityContextBuilder()
                .withAuthentication(
                    OAuth2TokenBuilder()
                        .withName("name")
                        .withRegistrationId(entity.provider)
                        .withAttribute("id", entity.providerId)
                        .withAuthority(SimpleGrantedAuthority("ROLE_USER"))
                        .build()
                ).build()
        )

        assertThrows(IllegalStateException::class.java) {
            accountService.createNewAccount(entity.copy(firstLogin = true))
        }.localizedMessage shouldContain "An account already exists for the logged in user."
    }

    @Test
    fun `Given new entity, when service saves new entity, then service should save in repository and return the same entity`() {
        val entity = AccountFactory.create()

        SecurityContextHolder.setContext(
            SecurityContextBuilder()
                .withAuthentication(
                    OAuth2TokenBuilder()
                        .withName(entity.name!!)
                        .withRegistrationId(entity.provider)
                        .withAttribute("id", entity.providerId)
                        .withAttribute("name", entity.name!!)
                        .withAuthority(SimpleGrantedAuthority("ROLE_USER"))
                        .build()
                ).build()
        )

        val newAccount = accountService.createNewAccount(entity.copy(firstLogin = true))

        newAccount.name shouldBeEqualTo entity.name
        newAccount.provider shouldBeEqualTo entity.provider
        newAccount.organisationName shouldBeEqualTo entity.organisationName
        newAccount.firstLogin shouldBeEqualTo false
    }
}
