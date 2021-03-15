package app.choppa.acceptance.domain.account

import app.choppa.domain.account.Account
import app.choppa.domain.account.AccountRepository
import app.choppa.domain.account.AccountService
import app.choppa.exception.UnsupportedProviderException
import app.choppa.support.builder.OAuth2TokenBuilder
import io.mockk.every
import io.mockk.mockkClass
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments.of
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import java.util.UUID.randomUUID
import java.util.stream.Stream

internal class AccountAuthenticationResolverTest {
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
    fun `Given unsupported provider Id key, when service tries to resolve account from Authentication token, then service should throw UnsupportedOAuth2ProviderException`() {
        every { securityContext.authentication } returns OAuth2TokenBuilder()
            .withName("Test")
            .withRegistrationId("provider")
            .withAttribute("UNSUPPORTED", "PROVIDER_ID")
            .withAuthority(SimpleGrantedAuthority("ROLE_USER"))
            .build()

        SecurityContextHolder.setContext(securityContext)

        assertThrows(UnsupportedProviderException::class.java) { service.resolveFromAuth() }
    }

    @Test
    fun `Given new account authentication, when service resolves account using Authentication token, then service should create a new account`() {
        val provider = "provider"
        val providerId = randomUUID().toString()

        every { securityContext.authentication } returns OAuth2TokenBuilder()
            .withName("name")
            .withRegistrationId(provider)
            .withAttribute("id", providerId)
            .withAuthority(SimpleGrantedAuthority("ROLE_USER"))
            .build()

        every { repository.findByProviderAndProviderId(provider, providerId) } returns null

        SecurityContextHolder.setContext(securityContext)

        val account = service.resolveFromAuth()

        account.firstLogin shouldBeEqualTo true
    }

    @ParameterizedTest
    @MethodSource("authenticationProvidersArgs")
    fun `Given valid authentication from supported provider X, when service resolves account using authentication token, then account should be resolved with correct attributes`(
        expected: Account,
        authentication: OAuth2AuthenticationToken
    ) {
        every { securityContext.authentication } returns authentication

        every {
            repository.findByProviderAndProviderId(expected.provider, expected.providerId)
        } returns expected

        SecurityContextHolder.setContext(securityContext)

        val account = service.resolveFromAuth()

        account.name shouldBeEqualTo expected.name
        account.profilePicture shouldBeEqualTo expected.profilePicture
        account.provider shouldBeEqualTo expected.provider
        account.providerId shouldBeEqualTo expected.providerId
        account.organisationName shouldBeEqualTo expected.organisationName
    }

    companion object {
        @JvmStatic
        fun authenticationProvidersArgs() = Stream.of(
            Account(
                provider = "github",
                providerId = "github_providerId",
                organisationName = "github_organisationName",
                profilePicture = "github_avatar_url",
                name = "github_name",
            ).run {
                of(
                    this,
                    OAuth2TokenBuilder()
                        .withRegistrationId(provider)
                        .withAttribute("id", providerId)
                        .withAttribute("avatar_url", "github_avatar_url")
                        .withAttribute("name", "github_name")
                        .build()
                )
            },
            Account(
                provider = "microsoft",
                providerId = "microsoft_providerId",
                organisationName = "microsoft_organisationName",
                profilePicture = "",
                name = "microsoft_name",
            ).run {
                of(
                    this,
                    OAuth2TokenBuilder()
                        .withRegistrationId(provider)
                        .withAttribute("sub", providerId)
                        .withAttribute("name", "microsoft_name")
                        .build()
                )
            },
            Account(
                provider = "google",
                providerId = "google_providerId",
                organisationName = "google_organisationName",
                profilePicture = "google_picture",
                name = "google_name",
            ).run {
                of(
                    this,
                    OAuth2TokenBuilder()
                        .withRegistrationId(provider)
                        .withAttribute("sub", providerId)
                        .withAttribute("picture", "google_picture")
                        .withAttribute("name", "google_name")
                        .build()
                )
            },
            Account(
                provider = "okta",
                providerId = "okta_providerId",
                organisationName = "okta_organisationName",
                profilePicture = "okta_profile",
                name = "okta_name",
            ).run {
                of(
                    this,
                    OAuth2TokenBuilder()
                        .withRegistrationId(provider)
                        .withAttribute("sub", providerId)
                        .withAttribute("profile", "okta_profile")
                        .withAttribute("name", "okta_name")
                        .build()
                )
            },
        )
    }
}
