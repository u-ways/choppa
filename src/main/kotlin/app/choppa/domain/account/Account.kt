package app.choppa.domain.account

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import org.hibernate.annotations.GenericGenerator
import java.time.Instant
import java.util.*
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
    val provider: String,

    @Column(name = "provider_id", columnDefinition = "VARCHAR(4096)")
    val providerId: String,

    @Column(name = "organisation_name", columnDefinition = "VARCHAR(100)")
    val organisationName: String,

    @Column(name = "create_date", columnDefinition = "TIMESTAMP WITH TIME ZONE", nullable = false)
    val createDate: Instant = Instant.now(),

    @Transient
    var name: String? = null,

    @Transient
    var profilePicture: String? = null,

    @Transient
    var firstLogin: Boolean = false,
) {
    override fun toString() = "Account(provider=$provider, providerId=$providerId, organisationName=$organisationName)"

    override fun hashCode(): Int = id.hashCode()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Account
        if (id != other.id) return false
        return true
    }

    companion object {
        val PLACEHOLDER_ACCOUNT: Account
            get() = Account(
                provider = "",
                providerId = "",
                organisationName = "",
            )
    }
}
