package org.choppa.model.squad

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import org.choppa.model.history.History
import org.choppa.model.member.Member
import org.choppa.model.tribe.Tribe
import org.hibernate.annotations.GenericGenerator
import java.util.UUID
import java.util.UUID.randomUUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType.EAGER
import javax.persistence.FetchType.LAZY
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.JoinTable
import javax.persistence.ManyToMany
import javax.persistence.ManyToOne
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "squad")
data class Squad @JsonCreator constructor(
    @Id
    @Column(name = "squad_id", columnDefinition = "uuid")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @JsonProperty("id")
    val id: UUID = randomUUID(),

    @Column(name = "name", columnDefinition = "VARCHAR(100)", nullable = false)
    @JsonProperty("name")
    val name: String,

    @ManyToOne(fetch = EAGER)
    @JsonIgnore
    @JoinColumn(name = "tribe", referencedColumnName = "tribe_id")
    var tribe: Tribe? = null,

    @ManyToMany(fetch = EAGER)
    @JoinTable(
        name = "squad_current_members",
        joinColumns = [JoinColumn(name = "squad_id", referencedColumnName = "squad_id")],
        inverseJoinColumns = [JoinColumn(name = "member_id", referencedColumnName = "member_id")]
    )
    @JsonIgnore
    val members: MutableList<Member> = mutableListOf(),

    @OneToMany(mappedBy = "squad", fetch = LAZY)
    @JsonIgnore
    val iterations: MutableList<History> = mutableListOf()
) {
    override fun toString() = "Squad(id=$id, name=$name, tribe=$tribe)"
}
