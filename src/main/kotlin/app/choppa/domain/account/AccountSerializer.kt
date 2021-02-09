package app.choppa.domain.account

import app.choppa.domain.base.BaseSerializer
import app.choppa.domain.base.BaseSerializer.QueryType.MEMBERS
import app.choppa.utils.Color.Companion.toRGBAHex
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider

class AccountSerializer(
    supportedClass: Class<Account>? = null
) : BaseSerializer<Account>(supportedClass) {
    override fun serialize(account: Account, gen: JsonGenerator, provider: SerializerProvider) {
        gen.writeStartObject()
        gen.writeStringField(account::name.name, account.name)
        gen.writeEndObject()
    }
}
