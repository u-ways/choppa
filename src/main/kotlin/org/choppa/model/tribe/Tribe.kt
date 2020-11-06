package org.choppa.model.tribe

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import org.choppa.model.history.History
import org.choppa.model.squad.Squad
import org.hibernate.annotations.GenericGenerator
import java.util.UUID
import java.util.UUID.randomUUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType.EAGER
import javax.persistence.FetchType.LAZY
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
    val id: UUID = randomUUID(),

    @Column(name = "name", columnDefinition = "VARCHAR(100)", nullable = false)
    @JsonProperty("name")
    val name: String,

    @OneToMany(mappedBy = "tribe", fetch = EAGER)
    @JsonIgnore
    val squads: MutableList<Squad> = mutableListOf(),

    @OneToMany(mappedBy = "tribe", fetch = LAZY)
    @JsonIgnore
    val iterations: MutableList<History> = mutableListOf()
) {
    override fun toString() = "Tribe(id=$id, name=$name)"
}
