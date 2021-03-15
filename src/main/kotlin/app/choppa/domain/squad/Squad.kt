package app.choppa.domain.squad

import app.choppa.domain.account.Account
import app.choppa.domain.account.Account.Companion.PLACEHOLDER_ACCOUNT
import app.choppa.domain.base.BaseModel
import app.choppa.domain.member.Member
import app.choppa.domain.member.Member.Companion.NO_MEMBERS
import app.choppa.domain.tribe.Tribe
import app.choppa.domain.tribe.Tribe.Companion.PLACEHOLDER_TRIBE
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import org.hibernate.annotations.GenericGenerator
import java.util.*
import java.util.UUID.randomUUID
import javax.persistence.*
import javax.persistence.FetchType.EAGER

@Entity
@Table(name = "squad")
@JsonSerialize(using = SquadSerializer::class)
@JsonDeserialize(using = SquadDeserializer::class)
data class Squad(
    @Id
    @Column(name = "squad_id", columnDefinition = "uuid")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    override val id: UUID = randomUUID(),

    @Column(name = "name", columnDefinition = "VARCHAR(100)", nullable = false)
    val name: String,

    @Column(name = "color", columnDefinition = "INTEGER")
    val color: Int,

    // TODO(U-ways) we might be able to lazy fetch this (default is eager)
    @ManyToOne
    @JoinColumn(name = "tribe", referencedColumnName = "tribe_id")
    val tribe: Tribe,

    @ManyToMany(fetch = EAGER)
    @JoinTable(
        name = "squad_current_members",
        joinColumns = [JoinColumn(name = "squad_id", referencedColumnName = "squad_id")],
        inverseJoinColumns = [JoinColumn(name = "member_id", referencedColumnName = "member_id")]
    )
    val members: MutableList<Member> = NO_MEMBERS,

    @ManyToOne
    @JoinColumn(name = "account_id", referencedColumnName = "account_id")
    override val account: Account,
) : BaseModel {
    override fun toString() = "Squad(id=$id, name=$name, tribe=$tribe)"

    override fun hashCode(): Int = id.hashCode()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Squad
        if (id != other.id) return false
        return true
    }

    companion object {
        val NO_SQUADS: MutableList<Squad>
            get() = mutableListOf()
        val PLACEHOLDER_SQUAD: Squad
            get() = Squad(name = "", color = 0, tribe = PLACEHOLDER_TRIBE, account = PLACEHOLDER_ACCOUNT)
    }
}
