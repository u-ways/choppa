package app.choppa.domain.tribe

import app.choppa.domain.account.Account
import app.choppa.domain.account.Account.Companion.PLACEHOLDER_ACCOUNT
import app.choppa.domain.base.BaseModel
import app.choppa.domain.squad.Squad
import app.choppa.domain.squad.Squad.Companion.NO_SQUADS
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import org.hibernate.annotations.BatchSize
import org.hibernate.annotations.Fetch
import org.hibernate.annotations.FetchMode.SELECT
import org.hibernate.annotations.GenericGenerator
import java.util.*
import java.util.UUID.randomUUID
import javax.persistence.*
import javax.persistence.FetchType.EAGER

@Entity
@Table(name = "tribe")
@JsonSerialize(using = TribeSerializer::class)
@JsonDeserialize(using = TribeDeserializer::class)
data class Tribe(
    @Id
    @Column(name = "tribe_id", columnDefinition = "uuid")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    override val id: UUID = randomUUID(),

    @Column(name = "name", columnDefinition = "VARCHAR(100)", nullable = false)
    val name: String,

    @Column(name = "color", columnDefinition = "INTEGER")
    val color: Int,

    @Fetch(SELECT)
    @BatchSize(size = 10)
    @OneToMany(mappedBy = "tribe", fetch = EAGER)
    val squads: MutableList<Squad> = NO_SQUADS,

    @ManyToOne
    @JoinColumn(name = "account_id", referencedColumnName = "account_id")
    override val account: Account,
) : BaseModel {
    override fun toString() = "Tribe(id=$id, name=$name)"

    override fun hashCode(): Int = id.hashCode()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Tribe
        if (id != other.id) return false
        return true
    }

    companion object {
        val PLACEHOLDER_TRIBE: Tribe
            get() = Tribe(name = "", color = 0, account = PLACEHOLDER_ACCOUNT)
    }
}
