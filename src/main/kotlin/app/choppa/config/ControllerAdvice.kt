package app.choppa.config

import app.choppa.exception.EmptyListException
import app.choppa.exception.EntityNotFoundException
import app.choppa.exception.UnprocessableEntityException
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.*
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.status
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.ServletWebRequest
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import java.time.Instant.now

@ControllerAdvice
class ControllerAdvice {

    @ExceptionHandler(value = [UnprocessableEntityException::class, HttpMessageNotReadableException::class])
    fun invalidPayLoad(e: Exception, req: ServletWebRequest): ResponseEntity<Map<String, Any>> =
        response(UNPROCESSABLE_ENTITY, "Invalid ${req.request.method} request payload.", req, e)

    @ExceptionHandler(value = [EntityNotFoundException::class, MethodArgumentTypeMismatchException::class])
    fun entityNotFound(e: Exception, req: ServletWebRequest): ResponseEntity<Map<String, Any>> =
        response(NOT_FOUND, "${req.request.method} failed - entity does not exist.", req, e)

    @ExceptionHandler(value = [EmptyListException::class])
    fun emptyList(e: Exception, req: ServletWebRequest): ResponseEntity<Map<String, Any>> =
        response(NO_CONTENT, "LIST failed - no entities exist for collection yet.", req, e)

    private fun response(
        status: HttpStatus,
        message: String,
        request: ServletWebRequest,
        error: Exception,
    ): ResponseEntity<Map<String, Any>> {
        val response: MutableMap<String, Any> = mutableMapOf()

        response["status"] = status.value()
        response["path"] = request.request.requestURI
        response["timestamp"] = now()
        response["message"] = message
        response["error"] = when (error.cause) {
            is Throwable -> error.cause!!.localizedMessage
            else -> error.localizedMessage
        }

        return status(status).body(response)
    }
}
