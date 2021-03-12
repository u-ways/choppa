package app.choppa.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(code = HttpStatus.I_AM_A_TEAPOT)
class UnsupportedProviderException(provider: String?) : RuntimeException("Unsupported OAuth Provider Id for [$provider]")
