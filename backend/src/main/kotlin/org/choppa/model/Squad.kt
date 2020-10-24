package org.choppa.model

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import org.choppa.model.relations.IterationHistory
import org.choppa.model.relations.SquadCurrentMembers
import org.hibernate.annotations.GenericGenerator
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.JoinColumn
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
    val id: UUID,

    @Column(name = "name", columnDefinition = "VARCHAR(100)", nullable = false)
    @JsonProperty("name")
    val name: String,

    @OneToMany(mappedBy = "squad")
    @JsonIgnore
    var members: MutableList<SquadCurrentMembers> = mutableListOf(),

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "tribe", referencedColumnName = "tribe_id")
    var tribe: Tribe?,

    @OneToMany(mappedBy = "squad")
    @JsonIgnore
    var iterations: MutableList<IterationHistory> = mutableListOf()
) {
    override fun toString() = "Squad(id=$id, name=$name)"
}
