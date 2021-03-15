package app.choppa.support.base

import app.choppa.domain.account.AccountService
import app.choppa.support.flyway.FlywayMigrationConfig
import app.choppa.support.testcontainers.TestDBContainer
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.BeforeEach
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@SpringBootTest
@Testcontainers
@Import(FlywayMigrationConfig::class)
@ActiveProfiles("test")
abstract class BaseServiceIT : Universe() {
    @Container
    private val testDBContainer: TestDBContainer = TestDBContainer.get()

    @MockkBean(relaxed = true)
    private lateinit var accountService: AccountService

    @BeforeEach
    internal fun authenticationSetup() {
        every { accountService.resolveFromAuth() } returns ACCOUNT
    }
}
