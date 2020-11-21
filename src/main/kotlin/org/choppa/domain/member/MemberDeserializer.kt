package org.choppa.domain.member

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import org.choppa.domain.base.BaseDeserializer
import org.choppa.domain.chapter.Chapter
import org.choppa.domain.chapter.Chapter.Companion.UNASSIGNED_ROLE
import org.choppa.exception.UnprocessableEntityException

class MemberDeserializer(supportedClass: Class<Member>? = null) : BaseDeserializer<Member>(supportedClass) {
    override fun deserialize(parser: JsonParser, ctx: DeserializationContext): Member {
        try {
            val node: JsonNode = parser.codec.readTree(parser)
            return when (node.size()) {
                0 -> Member(node.extractUUID())
                1 -> Member(node["id"].extractUUID())
                else -> {
                    val id = node["id"].extractUUID()
                    val name = node["name"].textValue()
                    val chapter = when (val child: JsonNode? = node["chapter"]) {
                        null -> UNASSIGNED_ROLE
                        else -> mapper.readValue("$child", Chapter::class.java)
                    }
                    Member(id, name, chapter)
                }
            }
        } catch (e: Exception) {
            throw UnprocessableEntityException("Unable to parse requested member entity.", e)
        }
    }
}
