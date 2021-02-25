package app.choppa.domain.account

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import org.hibernate.annotations.GenericGenerator
import java.util.*
import java.util.UUID.fromString
import java.util.UUID.randomUUID
import javax.persistence.*

@Entity
@Table(name = "account", uniqueConstraints = [UniqueConstraint(columnNames = ["provider", "provider_id"])])
@JsonSerialize(using = AccountSerializer::class)
data class Account(
    @Id
    @Column(name = "account_id", columnDefinition = "uuid")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    val id: UUID = randomUUID(),

    @Column(name = "provider", columnDefinition = "VARCHAR(100)")
    val provider: String = "PRO-$id".substring(0, 5),

    @Column(name = "provider_id", columnDefinition = "VARCHAR(4096)")
    val providerId: String = randomUUID().toString(),

    @Column(name = "organisation_name", columnDefinition = "VARCHAR(100)")
    val organisationName: String = "ORG-$id".substring(0, 15),

    @Transient
    var name: String = "ACC-$id".substring(0, 15),

    @Transient
    var profilePicture: String = "",

    @Transient
    var firstLogin: Boolean = false,
) {
    companion object {
        val UNASSIGNED_ACCOUNT = Account(
            id = fromString("00000000-0000-0000-0000-000000000000"),
            provider = "Unassigned Provider",
            providerId = "unassigned-provider-id",
            organisationName = "Unassigned Org",
            name = "Unassigned Account",
            profilePicture = "",
            firstLogin = false
        )

        val DEMO_ACCOUNT = Account(
            id = fromString("00000000-0000-0000-0000-000000000001"),
            provider = "choppa",
            providerId = "choppa-demo-account",
            organisationName = "Choppa Demo Org",
            name = "Choppa Demo",
            profilePicture = "",
            firstLogin = false
        )
    }

    override fun toString() = "Account(provider=$provider, providerId=$providerId, organisationName=$organisationName)"

    override fun hashCode(): Int = id.hashCode()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Account
        if (id != other.id) return false
        return true
    }
}
