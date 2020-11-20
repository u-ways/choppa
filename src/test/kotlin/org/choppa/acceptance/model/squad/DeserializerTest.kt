package org.choppa.acceptance.model.squad

import com.fasterxml.jackson.databind.ObjectMapper
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.sameInstance
import org.choppa.exception.UnprocessableEntityException
import org.choppa.model.squad.Squad
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.UUID.randomUUID

internal class DeserializerTest {
    private val white = -256
    private lateinit var squad: Squad
    private lateinit var mapper: ObjectMapper

    @BeforeEach
    internal fun setUp() {
        squad = Squad(color = white)
        mapper = ObjectMapper()
    }

    @Test
    fun `Given valid entity DTO, when deserialize, then it should return correct DAO`() {
        val validDto =
            """
            {
                "id": "squads/${squad.id}",
                "name": "${squad.name}",
                "color": "#ffffff",
                "tribe": {
                    "id": "tribes/${squad.tribe.id}",
                    "name": "${squad.tribe.name}"
                },
                "members": [
                    {
                        "id": "members/${randomUUID()}",
                        "name": "MEM-1",
                        "chapter": "chapters/${randomUUID()}"
                    },
                    {
                        "id": "members/${randomUUID()}",
                        "name": "MEM-2",
                        "chapter": "chapters/${randomUUID()}"
                    }
                ],
                "iterations": "iterations?squad=${squad.id}",
                "history": "history?squad=${squad.id}"
            }
            """.trimIndent()

        val dao = mapper.readValue(validDto, Squad::class.java)

        assertThat(dao.id, equalTo(squad.id))
        assertThat(dao.name, equalTo(squad.name))
        assertThat(dao.color, equalTo(squad.color))
    }

    @Test
    fun `Given valid entity DTO with uniform chapter, when deserialize, then it should return correct DAO`() {
        val validDto =
            """
            {
                "id": "squads/${squad.id}",
                "name": "${squad.name}",
                "tribe": "tribes/${squad.tribe.id}",
                "members": ["${randomUUID()}"],
                "iterations": "iterations?squad=${squad.id}",
                "history": "history?squad=${squad.id}"
            }
            """.trimIndent()

        val dao = mapper.readValue(validDto, Squad::class.java)

        assertThat(dao.id, equalTo(squad.id))
        assertThat(dao.name, equalTo(squad.name))
    }

    @Test
    fun `Given valid minimal entity DTO with name only, when deserialize, then it should return correct DAO`() {
        val validDto =
            """
            {
                "id": "squads/${squad.id}", 
                "name": "${squad.name}"
            }
            """.trimIndent()

        val dao = mapper.readValue(validDto, Squad::class.java)

        assertThat(dao.id, equalTo(squad.id))
        assertThat(dao.name, equalTo(squad.name))
    }

    @Test
    fun `Given valid uniform DTO, when deserialize, then it should return correct DAO type`() {
        val validDto = "\"squads/${squad.id}\""
        val dao = mapper.readValue(validDto, Squad::class.java)

        assertThat(dao.id, equalTo(squad.id))
        assertThat(dao::class, sameInstance(Squad::class))
    }

    @Test
    fun `Given invalid squad current members datatype DTO, when deserialize, then it should throw UnprocessableEntityException`() {
        val invalidDto =
            """
            {
                "id": "squads/${squad.id}", 
                "members": "not an array"
            }
            """.trimIndent()

        assertThrows(UnprocessableEntityException::class.java) {
            mapper.readValue(invalidDto, Squad::class.java)
        }
    }

    @Test
    fun `Given invalid entity DTO, when deserialize, then it should throw UnprocessableEntityException`() {
        val invalidDto = "{ invalidDto }"

        assertThrows(UnprocessableEntityException::class.java) {
            mapper.readValue(invalidDto, Squad::class.java)
        }
    }

    @Test
    fun `Given invalid color in entity DTO, when deserialize, then it it should throw UnprocessableEntityException`() {
        val invalidDto =
            """
            {
                "id": "squads/${squad.id}",
                "name": "${squad.name}",
                "color": "#00ff0"
            }
            """.trimIndent()

        assertThrows(UnprocessableEntityException::class.java) {
            mapper.readValue(invalidDto, Squad::class.java)
        }
    }
}
