package app.choppa.integration.domain.account

import app.choppa.domain.account.AccountController
import app.choppa.domain.account.AccountDemoSeed
import app.choppa.domain.account.AccountDemoSeed.Companion.DEMO_ORGANISATION_NAME
import app.choppa.domain.account.AccountDemoSeed.Companion.DEMO_PROVIDER
import app.choppa.domain.account.AccountService
import app.choppa.support.base.BaseControllerIT
import app.choppa.support.factory.AccountFactory
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import org.hamcrest.core.StringContains
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.HttpHeaders.LOCATION
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

@WebMvcTest(controllers = [AccountController::class])
internal class AccountControllerIT : BaseControllerIT() {
    @MockkBean
    private lateinit var service: AccountService

    @MockkBean(relaxed = true)
    private lateinit var accountDemoSeed: AccountDemoSeed

    @Nested
    @WithMockUser
    inner class HappyPath {
        @Test
        fun `GET logged in entity`() {
            val account = AccountFactory.create()
            every { service.resolveFromAuth() } returns account

            mvc.get("/api/accounts/me") {
                contentType = APPLICATION_JSON
                accept = APPLICATION_JSON
            }.andExpect {
                status { isOk }
                content { contentType(APPLICATION_JSON) }
                content { json(mapper.writeValueAsString(account)) }
            }
        }

        @Test
        fun `GET demo entity`() {
            val account = AccountFactory.create(
                provider = DEMO_PROVIDER,
                organisationName = DEMO_ORGANISATION_NAME
            )

            every { service.createDemoAccount() } returns account.copy(firstLogin = false)

            mvc.get("/api/accounts/demo") {
                contentType = APPLICATION_JSON
                accept = APPLICATION_JSON
                content = mapper.writeValueAsString(account)
            }.andExpect {
                status { isOk }
                content { contentType(APPLICATION_JSON) }
                content { json(mapper.writeValueAsString(account)) }
            }

            verify(exactly = 1) { accountDemoSeed.create(account) }
        }

        @Test
        fun `POST new entity`() {
            val account = AccountFactory.create()

            every { service.createNewAccount(account) } returns account.copy(firstLogin = false)

            mvc.post("/api/accounts") {
                contentType = APPLICATION_JSON
                accept = APPLICATION_JSON
                content = mapper.writeValueAsString(account)
            }.andExpect {
                status { isCreated }
                header { string(LOCATION, StringContains("/api/accounts/me")) }
            }
        }
    }
}
