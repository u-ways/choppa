package org.choppa.domain.squad

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import org.choppa.domain.base.BaseModel
import org.choppa.domain.history.History
import org.choppa.domain.member.Member
import org.choppa.domain.member.Member.Companion.NO_MEMBERS
import org.choppa.domain.tribe.Tribe
import org.choppa.domain.tribe.Tribe.Companion.UNASSIGNED_TRIBE
import org.choppa.utils.Color.Companion.GREY
import org.hibernate.annotations.GenericGenerator
import java.util.UUID
import java.util.UUID.fromString
import java.util.UUID.randomUUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType.EAGER
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.JoinTable
import javax.persistence.ManyToMany
import javax.persistence.ManyToOne
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "squad")
@JsonSerialize(using = SquadSerializer::class)
@JsonDeserialize(using = SquadDeserializer::class)
data class Squad @JsonCreator constructor(
    @Id
    @Column(name = "squad_id", columnDefinition = "uuid")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @JsonProperty("id")
    override val id: UUID = randomUUID(),

    @Column(name = "name", columnDefinition = "VARCHAR(100)", nullable = false)
    @JsonProperty("name")
    val name: String = "SQ-$id".substring(0, 15),

    @Column(name = "color", columnDefinition = "INTEGER")
    @JsonProperty("color")
    val color: Int = GREY,

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "tribe", referencedColumnName = "tribe_id")
    val tribe: Tribe = UNASSIGNED_TRIBE,

    @ManyToMany(fetch = EAGER)
    @JoinTable(
        name = "squad_current_members",
        joinColumns = [JoinColumn(name = "squad_id", referencedColumnName = "squad_id")],
        inverseJoinColumns = [JoinColumn(name = "member_id", referencedColumnName = "member_id")]
    )
    @JsonIgnore
    val members: MutableList<Member> = NO_MEMBERS,

    @OneToMany(mappedBy = "squad")
    @JsonIgnore
    val history: MutableList<History> = mutableListOf()
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
        val UNASSIGNED_SQUAD = Squad(fromString("00000000-0000-0000-0000-000000000000"), "Unassigned Members")
    }
}
