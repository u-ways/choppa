package app.choppa.domain.member

import app.choppa.domain.account.Account.Companion.PLACEHOLDER_ACCOUNT
import app.choppa.domain.base.BaseDeserializer
import app.choppa.domain.chapter.Chapter
import app.choppa.domain.chapter.Chapter.Companion.PLACEHOLDER_CHAPTER
import app.choppa.domain.member.Member.Companion.PLACEHOLDER_MEMBER
import app.choppa.exception.UnprocessableEntityException
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode

class MemberDeserializer(supportedClass: Class<Member>? = null) : BaseDeserializer<Member>(supportedClass) {
    override fun deserialize(parser: JsonParser, ctx: DeserializationContext): Member {
        try {
            val node: JsonNode = parser.codec.readTree(parser)
            return when (node.size()) {
                0 -> PLACEHOLDER_MEMBER.copy(id = node.extractUUID())
                1 -> PLACEHOLDER_MEMBER.copy(id = node["id"].extractUUID())
                else -> {
                    val id = node["id"].extractUUID()
                    val name = node["name"].textValue()
                    val chapter = when (val child: JsonNode? = node["chapter"]) {
                        null -> PLACEHOLDER_CHAPTER
                        else -> mapper.readValue("$child", Chapter::class.java)
                    }
                    val active = when (val child: JsonNode? = node["active"]) {
                        null -> true
                        else -> child.booleanValue()
                    }
                    Member(id, name, chapter, active, PLACEHOLDER_ACCOUNT)
                }
            }
        } catch (e: Exception) {
            throw UnprocessableEntityException("Unable to parse requested member entity.", e)
        }
    }
}
