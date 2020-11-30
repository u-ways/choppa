package app.choppa.acceptance.domain.tribe

import app.choppa.domain.tribe.Tribe
import app.choppa.exception.UnprocessableEntityException
import com.fasterxml.jackson.databind.ObjectMapper
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.sameInstance
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class TribeDeserializerTest {
    private val black = 255
    private lateinit var tribe: Tribe
    private lateinit var mapper: ObjectMapper

    @BeforeEach
    internal fun setUp() {
        tribe = Tribe(color = black)
        mapper = ObjectMapper()
    }

    @Test
    fun `Given valid entity DTO, when deserialize, then it should return correct DAO`() {
        val validDto =
            """
            {
                "id": "tribes/${tribe.id}",
                "name": "${tribe.name}",
                "color": "#000000",
                "squads": "squads?tribe=${tribe.id}",
                "iterations": "iterations?tribe=${tribe.id}",
                "history": "history?tribe=${tribe.id}"
            }
            """.trimIndent()

        val dao = mapper.readValue(validDto, Tribe::class.java)

        assertThat(dao.id, equalTo(tribe.id))
        assertThat(dao.name, equalTo(tribe.name))
        assertThat(dao.color, equalTo(tribe.color))
    }

    @Test
    fun `Given valid minimal entity DTO, when deserialize, then it should return correct DAO`() {
        val validDto =
            """
            { 
                "id": "tribes/${tribe.id}", 
                "name": "${tribe.name}"
            }
            """.trimIndent()

        val dao = mapper.readValue(validDto, Tribe::class.java)

        assertThat(dao.id, equalTo(tribe.id))
        assertThat(dao.name, equalTo(tribe.name))
    }

    @Test
    fun `Given valid uniform DTO, when deserialize, then it should return correct DAO type`() {
        val validDto = "\"tribes/${tribe.id}\""
        val dao = mapper.readValue(validDto, Tribe::class.java)

        assertThat(dao.id, equalTo(tribe.id))
        assertThat(dao::class, sameInstance(Tribe::class))
    }

    @Test
    fun `Given invalid entity DTO, when deserialize, then it should throw UnprocessableEntityException`() {
        val invalidDto = "{ invalidDto }"

        assertThrows(UnprocessableEntityException::class.java) {
            mapper.readValue(invalidDto, Tribe::class.java)
        }
    }

    @Test
    fun `Given invalid color in entity DTO, when deserialize, then it it should throw UnprocessableEntityException`() {
        val invalidDto =
            """
            {
                "id": "tribes/${tribe.id}",
                "name": "${tribe.name}",
                "color": "ABCDXYZ"
            }
            """.trimIndent()

        assertThrows(UnprocessableEntityException::class.java) {
            mapper.readValue(invalidDto, Tribe::class.java)
        }
    }
}
