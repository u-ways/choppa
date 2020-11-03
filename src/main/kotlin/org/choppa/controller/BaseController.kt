package org.choppa.controller

import org.choppa.helpers.exception.EmptyListException
import org.choppa.helpers.exception.EntityNotFoundException
import org.choppa.helpers.exception.UnprocessableEntityException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.HttpStatus.NO_CONTENT
import org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.status
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.ServletWebRequest
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest
import java.net.URI
import java.time.Instant.now

@ControllerAdvice
class BaseController(@Autowired private val env: Environment) {
    companion object {
        /**
         * Standard path for an identification of a singular resource.
         */
        const val ID_PATH: String = "{id}"

        /**
         * Returns the location of the current URI request with the values expanded.
         */
        fun location(path: String, vararg uriVariableValues: Any): URI =
            fromCurrentRequest().path("/$path").buildAndExpand(*uriVariableValues).toUri()
    }

    @ExceptionHandler(value = [UnprocessableEntityException::class, HttpMessageNotReadableException::class])
    fun invalidPayLoad(e: HttpMessageNotReadableException, req: ServletWebRequest): ResponseEntity<Map<String, Any>> =
        response(UNPROCESSABLE_ENTITY, "Invalid ${req.request.method} request payload.", req, e)

    @ExceptionHandler(value = [EntityNotFoundException::class, MethodArgumentTypeMismatchException::class])
    fun entityNotFound(e: Exception, req: ServletWebRequest): ResponseEntity<Map<String, Any>> =
        response(NOT_FOUND, "${req.request.method} failed - entity does not exist.", req, e)

    @ExceptionHandler(value = [EmptyListException::class])
    fun emptyList(e: EmptyListException, req: ServletWebRequest): ResponseEntity<Map<String, Any>> =
        response(NO_CONTENT, "LIST failed - no entities exist for collection yet.", req, e)

    private fun response(
        status: HttpStatus,
        message: String,
        request: ServletWebRequest,
        error: Exception
    ): ResponseEntity<Map<String, Any>> {
        val response: MutableMap<String, Any> = mutableMapOf()

        response["status"] = status.value()
        response["path"] = request.request.requestURI
        response["timestamp"] = now()
        response["message"] = when {
            env.activeProfiles.contains("prod") -> message
            else -> error.localizedMessage
        }

        return status(status).body(response)
    }
}
