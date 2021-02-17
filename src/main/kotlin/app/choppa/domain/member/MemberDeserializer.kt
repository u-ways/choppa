package app.choppa.domain.member

import app.choppa.domain.base.BaseDeserializer
import app.choppa.domain.chapter.Chapter
import app.choppa.domain.chapter.Chapter.Companion.UNASSIGNED_ROLE
import app.choppa.exception.UnprocessableEntityException
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode

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
                    val active = when (val child: JsonNode? = node["active"]) {
                        null -> true
                        else -> child.booleanValue()
                    }
                    val chapter = when (val child: JsonNode? = node["chapter"]) {
                        null -> UNASSIGNED_ROLE
                        else -> mapper.readValue("$child", Chapter::class.java)
                    }
                    Member(id, name, chapter, active)
                }
            }
        } catch (e: Exception) {
            throw UnprocessableEntityException("Unable to parse requested member entity.", e)
        }
    }
}
