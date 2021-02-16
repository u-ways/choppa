package app.choppa.domain.account

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import org.hibernate.annotations.GenericGenerator
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "accounts")
@JsonSerialize(using = AccountSerializer::class)
data class Account(
    @Id
    @Column(name = "account_id", columnDefinition = "uuid")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    val id: UUID = UUID.randomUUID(),

    @Column(name = "provider", columnDefinition = "VARCHAR(100)")
    val provider: String,

    @Column(name = "provider_id", columnDefinition = "VARCHAR(4096)")
    val providerId: String,

    @Transient
    var name: String = "",

    @Column(name = "organisation_name", columnDefinition = "VARCHAR(4096)")
    val organisationName: String,

    @Transient
    var profilePicture: String = "",

    @Transient
    var firstLogin: Boolean = false,
) {
    companion object {
        fun createFirstLogin(provider: String, providerId: String, name: String): Account = Account(
            UUID.randomUUID(),
            provider,
            providerId,
            name,
            "",
            "",
            true
        )
    }

    override fun toString() = "Account(provider=$provider, providerId=$providerId, name=$name)"
}
