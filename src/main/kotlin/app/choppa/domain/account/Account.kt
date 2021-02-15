package app.choppa.domain.account

import com.fasterxml.jackson.databind.annotation.JsonSerialize

@JsonSerialize(using = AccountSerializer::class)
data class Account(
    val provider: String,
    val providerId: String,
    val name: String = "",
) {
    override fun toString() = "Account(provider=$provider, providerId=$providerId, name=$name)"
}
