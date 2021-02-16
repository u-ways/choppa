package app.choppa.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(code = HttpStatus.I_AM_A_TEAPOT)
class NoOAuth2ProviderFoundException(provider: String?) : RuntimeException("No OAuth2UserToAccountConverter found for $provider")
