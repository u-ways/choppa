package choppaorg.choppa.model

import choppaorg.choppa.model.relations.IterationHistory
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import org.hibernate.annotations.GenericGenerator
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "tribe")
data class Tribe @JsonCreator constructor(
        @Id
        @Column(name = "tribe_id", columnDefinition = "uuid")
        @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
        @JsonProperty("id")
        val id: UUID,

        @Column(name = "tribe_name")
        @JsonProperty("name")
        val name: String,

        @OneToMany(fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST])
        @JoinTable(
                name = "tribe_current_squads",
                joinColumns = [JoinColumn(name = "tribe_id")],
                inverseJoinColumns = [JoinColumn(name = "squad_id")])
        @JsonIgnore
        var squads: MutableList<Squad> = mutableListOf(),

        @OneToMany(mappedBy = "tribe")
        @JsonIgnore
        var iterations: MutableList<IterationHistory> = mutableListOf()
) {
        override fun toString() = "Tribe(id=$id, name=$name)"
}