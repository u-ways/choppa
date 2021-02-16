package app.choppa.domain.account

import app.choppa.domain.base.BaseSerializer
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider

class AccountSerializer(
    supportedClass: Class<Account>? = null
) : BaseSerializer<Account>(supportedClass) {
    override fun serialize(account: Account, gen: JsonGenerator, provider: SerializerProvider) {
        gen.writeStartObject()
        gen.writeStringField(account::id.name, account.id.toString())
        gen.writeStringField(account::provider.name, account.provider)
        gen.writeStringField(account::providerId.name, account.providerId)
        gen.writeStringField(account::name.name, account.name)
        gen.writeStringField(account::organisationName.name, account.organisationName)
        gen.writeStringField(account::profilePicture.name, account.profilePicture)
        gen.writeBooleanField(account::firstLogin.name, account.firstLogin)
        gen.writeEndObject()
    }
}
