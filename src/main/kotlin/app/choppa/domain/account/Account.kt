package app.choppa.domain.account

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import org.springframework.security.oauth2.core.user.OAuth2User

@JsonSerialize(using = AccountSerializer::class)
data class Account(
    val name: String,
) {
    constructor(oauth2User: OAuth2User) : this(
        oauth2User.getAttribute<String>("name")!!
    )

    override fun toString() = "Account(name=$name)"
}
