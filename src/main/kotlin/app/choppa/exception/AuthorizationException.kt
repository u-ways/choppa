package app.choppa.exception

import org.springframework.http.HttpStatus.UNAUTHORIZED
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(code = UNAUTHORIZED)
class AuthorizationException(msg: String) : RuntimeException(msg)
