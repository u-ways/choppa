package app.choppa.domain.squad

import app.choppa.domain.account.Account.Companion.PLACEHOLDER_ACCOUNT
import app.choppa.domain.base.BaseDeserializer
import app.choppa.domain.member.Member
import app.choppa.domain.member.Member.Companion.NO_MEMBERS
import app.choppa.domain.squad.Squad.Companion.PLACEHOLDER_SQUAD
import app.choppa.domain.tribe.Tribe
import app.choppa.domain.tribe.Tribe.Companion.PLACEHOLDER_TRIBE
import app.choppa.exception.UnprocessableEntityException
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode

class SquadDeserializer(supportedClass: Class<Squad>? = null) : BaseDeserializer<Squad>(supportedClass) {
    override fun deserialize(parser: JsonParser, ctx: DeserializationContext): Squad {
        try {
            val node: JsonNode = parser.codec.readTree(parser)
            return when (node.size()) {
                0 -> PLACEHOLDER_SQUAD.copy(id = node.extractUUID())
                1 -> PLACEHOLDER_SQUAD.copy(id = node["id"].extractUUID())
                else -> {
                    val id = node["id"].extractUUID()
                    val name = node["name"].textValue()
                    val color = node.extractColor()
                    val tribe = when (val child: JsonNode? = node["tribe"]) {
                        null -> PLACEHOLDER_TRIBE
                        else -> mapper.readValue("$child", Tribe::class.java)
                    }
                    val members = when (val children: JsonNode? = node["members"]) {
                        null -> NO_MEMBERS
                        else -> children.map { mapper.readValue("$it", Member::class.java) }.toMutableList()
                    }
                    Squad(id, name, color, tribe, members, PLACEHOLDER_ACCOUNT)
                }
            }
        } catch (e: Exception) {
            throw UnprocessableEntityException("Unable to parse requested squad entity.", e)
        }
    }
}
