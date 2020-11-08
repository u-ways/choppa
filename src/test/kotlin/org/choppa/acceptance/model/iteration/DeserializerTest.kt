package org.choppa.acceptance.model.iteration

import com.fasterxml.jackson.databind.ObjectMapper
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.sameInstance
import org.choppa.exception.UnprocessableEntityException
import org.choppa.model.iteration.Iteration
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Instant.ofEpochMilli

internal class DeserializerTest {
    private lateinit var iteration: Iteration
    private lateinit var mapper: ObjectMapper

    @BeforeEach
    internal fun setUp() {
        iteration = Iteration()
        mapper = ObjectMapper()
    }

    @Test
    fun `Given valid entity DTO, when deserialize, then it should return correct DAO`() {
        val validDto =
            """
            {
                "id": "iterations/${iteration.id}",
                "number": ${iteration.number},
                "startDate": ${iteration.startDate.toEpochMilli()},
                "endDate": ${iteration.endDate.toEpochMilli()}
            }
            """.trimIndent()

        val dao = mapper.readValue(validDto, Iteration::class.java)

        assertThat(dao.id, equalTo(iteration.id))
        assertThat(dao.number, equalTo(iteration.number))
        assertThat(dao.startDate, equalTo(ofEpochMilli(iteration.startDate.toEpochMilli())))
        assertThat(dao.endDate, equalTo(ofEpochMilli(iteration.endDate.toEpochMilli())))
    }

    @Test
    fun `Given valid uniform DTO, when deserialize, then it should return correct DAO type`() {
        val validDto = "\"iterations/${iteration.id}\""
        val dao = mapper.readValue(validDto, Iteration::class.java)

        assertThat(dao.id, equalTo(iteration.id))
        assertThat(dao::class, sameInstance(Iteration::class))
    }

    @Test
    fun `Given invalid entity DTO, when deserialize, then it should throw UnprocessableEntityException`() {
        val invalidDto = "{ invalidDto }"

        Assertions.assertThrows(UnprocessableEntityException::class.java) {
            mapper.readValue(invalidDto, Iteration::class.java)
        }
    }
}
