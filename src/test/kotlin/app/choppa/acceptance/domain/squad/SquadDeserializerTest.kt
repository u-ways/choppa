package app.choppa.acceptance.domain.squad

import app.choppa.domain.squad.Squad
import app.choppa.exception.UnprocessableEntityException
import app.choppa.support.factory.SquadFactory
import com.fasterxml.jackson.databind.ObjectMapper
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.sameInstance
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.UUID.randomUUID

internal class SquadDeserializerTest {
    private val white = -1
    private lateinit var squad: Squad
    private lateinit var mapper: ObjectMapper

    @BeforeEach
    internal fun setUp() {
        squad = SquadFactory.create(color = white)
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
                        "chapter": {
                            "id": "chapters/${randomUUID()}",
                            "name": "CH-1",
                            "members": "members?chapter=${randomUUID()}"
                        }
                    },
                    {
                        "id": "members/${randomUUID()}",
                        "name": "MEM-2",
                        "chapter": {
                            "id": "chapters/${randomUUID()}",
                            "name": "CH-2",
                            "members": "members?chapter=${randomUUID()}"
                        }
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
