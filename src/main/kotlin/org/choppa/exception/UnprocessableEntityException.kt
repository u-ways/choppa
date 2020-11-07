package org.choppa.exception

class UnprocessableEntityException(msg: String, cause: Exception) : RuntimeException(msg, cause)
