package org.choppa.acceptance.domain.member

import com.fasterxml.jackson.databind.ObjectMapper
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.sameInstance
import org.choppa.domain.member.Member
import org.choppa.exception.UnprocessableEntityException
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class MemberDeserializerTest {
    private lateinit var member: Member
    private lateinit var mapper: ObjectMapper

    @BeforeEach
    internal fun setUp() {
        member = Member()
        mapper = ObjectMapper()
    }

    @Test
    fun `Given valid entity DTO, when deserialize, then it should return correct DAO`() {
        val validDto =
            """
            {
                "id": "members/${member.id}",
                "name": "${member.name}",
                "chapter": {
                    "id": "chapters/${member.chapter.id}",
                    "name": "${member.chapter.name}",
                    "members": "members?chapter=${member.chapter.id}"
                },
                "squads": "squads?member=${member.id}",
                "iterations": "iterations?member=${member.id}",
                "history": "history?member=${member.id}"
            }
            """.trimIndent()

        val dao = mapper.readValue(validDto, Member::class.java)

        assertThat(dao.id, equalTo(member.id))
        assertThat(dao.name, equalTo(member.name))
    }

    @Test
    fun `Given valid entity DTO with uniform chapter, when deserialize, then it should return correct DAO`() {
        val validDto =
            """
            {
                "id": "members/${member.id}",
                "name": "${member.name}",
                "chapter": "chapters/${member.chapter.id}",
                "squads": "squads?member=${member.id}",
                "iterations": "iterations?member=${member.id}",
                "history": "history?member=${member.id}"
            }
            """.trimIndent()

        val dao = mapper.readValue(validDto, Member::class.java)

        assertThat(dao.id, equalTo(member.id))
        assertThat(dao.name, equalTo(member.name))
    }

    @Test
    fun `Given valid minimal entity DTO, when deserialize, then it should return correct DAO`() {
        val validDto =
            """
            { 
                "id": "members/${member.id}", 
                "name": "${member.name}"
            }
            """.trimIndent()

        val dao = mapper.readValue(validDto, Member::class.java)

        assertThat(dao.id, equalTo(member.id))
        assertThat(dao.name, equalTo(member.name))
    }

    @Test
    fun `Given valid uniform DTO, when deserialize, then it should return correct DAO type`() {
        val validDto = "\"members/${member.id}\""
        val dao = mapper.readValue(validDto, Member::class.java)

        assertThat(dao.id, equalTo(member.id))
        assertThat(dao::class, sameInstance(Member::class))
    }

    @Test
    fun `Given invalid entity DTO, when deserialize, then it should throw UnprocessableEntityException`() {
        val invalidDto = "{ invalidDto }"

        assertThrows(UnprocessableEntityException::class.java) {
            mapper.readValue(invalidDto, Member::class.java)
        }
    }
}
