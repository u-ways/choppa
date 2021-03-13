package app.choppa.acceptance.domain.base

import app.choppa.domain.account.Account
import app.choppa.domain.base.BaseController
import app.choppa.domain.base.BaseModel
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*
import java.util.UUID.randomUUID

class BaseControllerTest {

    private lateinit var testAccount: Account

    @BeforeEach
    internal fun setUp() {
        testAccount = Account()
    }

    @Test
    fun `put singular resource request should validate endpoint id against updatedEntity id`() {
        val controller = object : BaseController<BaseModel>(mockk(relaxed = true)) {}

        assertThrows<IllegalArgumentException> {
            controller.put(
                randomUUID(),
                object : BaseModel {
                    override val id: UUID = randomUUID()
                    override val account: Account = testAccount
                }
            )
        }
    }

    @Test
    fun `post singular resource request should validate endpoint id against newEntity id`() {
        val controller = object : BaseController<BaseModel>(mockk(relaxed = true)) {}
        assertThrows<IllegalArgumentException> {
            controller.post(
                randomUUID(),
                object : BaseModel {
                    override val id: UUID = randomUUID()
                    override val account: Account = testAccount
                }
            )
        }
    }
}
