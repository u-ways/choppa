package app.choppa.integration.domain.account

import app.choppa.domain.account.Account
import app.choppa.domain.account.AccountController
import app.choppa.domain.account.AccountService
import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.hamcrest.core.StringContains
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.HttpHeaders.LOCATION
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

@WebMvcTest(controllers = [AccountController::class])
@ActiveProfiles("test")
internal class AccountControllerIT @Autowired constructor(
    private val mvc: MockMvc,
    private val mapper: ObjectMapper,
) {
    @MockkBean
    private lateinit var accountService: AccountService

    @Nested
    @WithMockUser
    inner class HappyPath {
        @Test
        fun `GET logged in account`() {
            val account = Account()
            every { accountService.resolveFromAuth() } returns account

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
        fun `GET create demo authorisation`() {
            every { accountService.createDemoAuthorisation() } returns Unit

            mvc.get("/api/accounts/demo") {
                contentType = APPLICATION_JSON
                accept = APPLICATION_JSON
            }.andExpect {
                status { isOk }
            }
        }

        @Test
        fun `POST new entity`() {
            val account = Account()

            every { accountService.createNewAccount(account) } returns account.copy(firstLogin = false)

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
