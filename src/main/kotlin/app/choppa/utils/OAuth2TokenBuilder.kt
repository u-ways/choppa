package app.choppa.utils

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.security.oauth2.core.user.OAuth2User

data class OAuth2TokenBuilder(
    private var name: String? = null,
    private var registrationId: String? = null,
    private val attributes: MutableMap<String, Any> = mutableMapOf(),
    private val authorities: MutableCollection<GrantedAuthority> = mutableListOf(),
) {
    fun withName(value: String) = apply { this.name = value }
    fun withRegistrationId(value: String) = apply { this.registrationId = value }
    fun withAttribute(key: String, value: Any) = apply { this.attributes[key] = value }
    fun withAuthority(value: GrantedAuthority) = apply { this.authorities.add(value) }

    fun build(): OAuth2AuthenticationToken = OAuth2AuthenticationToken(
        object : OAuth2User {
            override fun getName(): String = this@OAuth2TokenBuilder.name ?: error("OAuth2 principal must have a name.")
            override fun getAttributes(): MutableMap<String, Any> = this@OAuth2TokenBuilder.attributes
            override fun getAuthorities(): MutableCollection<out GrantedAuthority> = this@OAuth2TokenBuilder.authorities
        },
        this@OAuth2TokenBuilder.authorities,
        this@OAuth2TokenBuilder.registrationId ?: error("authorizedClientRegistrationId cannot be null.")
    )
}
