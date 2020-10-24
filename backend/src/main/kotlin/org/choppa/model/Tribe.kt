package org.choppa.model

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import org.choppa.model.relations.IterationHistory
import org.hibernate.annotations.GenericGenerator
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "tribe")
data class Tribe @JsonCreator constructor(
    @Id
    @Column(name = "tribe_id", columnDefinition = "uuid")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @JsonProperty("id")
    val id: UUID,

    @Column(name = "name", columnDefinition = "VARCHAR(100)", nullable = false)
    @JsonProperty("name")
    val name: String,

    @OneToMany(mappedBy = "tribe")
    @JsonIgnore
    var squads: MutableList<Squad> = mutableListOf(),

    @OneToMany(mappedBy = "tribe")
    @JsonIgnore
    var iterations: MutableList<IterationHistory> = mutableListOf()
) {
    override fun toString() = "Tribe(id=$id, name=$name)"
}
