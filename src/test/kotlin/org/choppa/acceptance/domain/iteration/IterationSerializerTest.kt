package org.choppa.acceptance.domain.iteration

import com.fasterxml.jackson.databind.ObjectMapper
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.nfeld.jsonpathkt.JsonPath
import com.nfeld.jsonpathkt.extension.read
import org.choppa.domain.iteration.Iteration
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class IterationSerializerTest {
    private lateinit var iteration: Iteration
    private lateinit var mapper: ObjectMapper

    @BeforeEach
    internal fun setUp() {
        iteration = Iteration()
        mapper = ObjectMapper()
    }

    @Test
    fun `Given entity DAO, when serialize, then it should return correct uniform DTO`() {
        val uniformDto = JsonPath.parse(mapper.writeValueAsString(iteration))

        val id = uniformDto?.read<String>("$.id")
        val number = uniformDto?.read<Int>("$.number")
        val startDate = uniformDto?.read<Long>("$.startDate")
        val endDate = uniformDto?.read<Long>("$.endDate")

        assertThat(id, equalTo("iterations/${iteration.id}"))
        assertThat(number, equalTo(iteration.number))
        assertThat(startDate, equalTo(iteration.startDate.toEpochMilli()))
        assertThat(endDate, equalTo(iteration.endDate.toEpochMilli()))
    }
}
