package org.choppa.domain.tribe

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import org.choppa.domain.history.History
import org.choppa.domain.squad.Squad
import org.choppa.utils.Color.Companion.GREY
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
@JsonSerialize(using = TribeSerializer::class)
@JsonDeserialize(using = TribeDeserializer::class)
@Suppress("EqualsOrHashCode")
data class Tribe @JsonCreator constructor(
    @Id
    @Column(name = "tribe_id", columnDefinition = "uuid")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @JsonProperty("id")
    val id: UUID = randomUUID(),

    @Column(name = "name", columnDefinition = "VARCHAR(100)", nullable = false)
    @JsonProperty("name")
    val name: String = "TR-$id".substring(0, 15),

    @Column(name = "color", columnDefinition = "INTEGER")
    @JsonProperty("color")
    val color: Int = GREY,

    @OneToMany(mappedBy = "tribe", fetch = EAGER)
    @JsonIgnore
    val squads: MutableList<Squad> = mutableListOf(),

    @OneToMany(mappedBy = "tribe", fetch = LAZY)
    @JsonIgnore
    val history: MutableList<History> = mutableListOf()
) {
    override fun toString() = "Tribe(id=$id, name=$name)"
    override fun hashCode(): Int = id.hashCode()

    companion object {
        val UNASSIGNED_TRIBE = Tribe(UUID.fromString("00000000-0000-0000-0000-000000000000"), "Unassigned Squad")
    }
}
