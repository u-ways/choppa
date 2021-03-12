package app.choppa.support.builder

import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContext

data class SecurityContextBuilder(
    private var authentication: Authentication? = null,
) {
    fun withAuthentication(value: Authentication) = apply { this.authentication = value }

    fun build(): SecurityContext = object : SecurityContext {
        private var authentication: Authentication? = null

        override fun getAuthentication(): Authentication =
            this@SecurityContextBuilder.authentication ?: error("Security Context Requires Authentication value")
        override fun setAuthentication(authentication: Authentication?) {
            this.authentication = authentication
        }
    }
}
