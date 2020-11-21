package org.choppa.domain.squad

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import org.choppa.domain.base.BaseDeserializer
import org.choppa.domain.member.Member
import org.choppa.domain.member.Member.Companion.NO_MEMBERS
import org.choppa.domain.tribe.Tribe
import org.choppa.domain.tribe.Tribe.Companion.UNASSIGNED_TRIBE
import org.choppa.exception.UnprocessableEntityException

class SquadDeserializer(supportedClass: Class<Squad>? = null) : BaseDeserializer<Squad>(supportedClass) {
    override fun deserialize(parser: JsonParser, ctx: DeserializationContext): Squad {
        try {
            val node: JsonNode = parser.codec.readTree(parser)
            return when (node.size()) {
                0 -> Squad(node.extractUUID())
                1 -> Squad(node["id"].extractUUID())
                else -> {
                    val id = node["id"].extractUUID()
                    val name = node["name"].textValue()
                    val color = node.extractColor()
                    val tribe = when (val child: JsonNode? = node["tribe"]) {
                        null -> UNASSIGNED_TRIBE
                        else -> mapper.readValue("$child", Tribe::class.java)
                    }
                    val members = when (val children: JsonNode? = node["members"]) {
                        null -> NO_MEMBERS
                        else -> {
                            require(children.isArray)
                            children.map { mapper.readValue("$it", Member::class.java) }.toMutableList()
                        }
                    }
                    Squad(id, name, color, tribe, members)
                }
            }
        } catch (e: Exception) {
            throw UnprocessableEntityException("Unable to parse requested squad entity.", e)
        }
    }
}
