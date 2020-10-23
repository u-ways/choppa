package choppaorg.choppa.model.relations

import choppaorg.choppa.model.Iteration
import choppaorg.choppa.model.Member
import choppaorg.choppa.model.Squad
import choppaorg.choppa.model.Tribe
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable
import java.util.*
import javax.persistence.*

@Entity
@IdClass(IterationHistoryId::class)
@Table(name = "iteration_history")
data class IterationHistory(
        @Id
        @ManyToOne
        @JoinColumn(name = "iteration_id", referencedColumnName = "iteration_id", columnDefinition = "uuid")
        @JsonProperty("iteration")
        val iteration: Iteration,

        @Id
        @ManyToOne
        @JoinColumn(name = "tribe_id", referencedColumnName = "tribe_id", columnDefinition = "uuid")
        @JsonProperty("tribe")
        val tribe: Tribe,

        @Id
        @ManyToOne
        @JoinColumn(name = "squad_id", referencedColumnName = "squad_id", columnDefinition = "uuid")
        @JsonProperty("squad")
        val squad: Squad,

        @Id
        @ManyToOne
        @JoinColumn(name = "member_id", referencedColumnName = "member_id", columnDefinition = "uuid")
        @JsonProperty("member")
        val member: Member
) {
        override fun toString() = "IterationHistory(iteration=$iteration, tribe=$tribe, squad=$squad, member=$member)"
}

class IterationHistoryId(
        val iteration: UUID = UUID.randomUUID(),
        val tribe: UUID = UUID.randomUUID(),
        val squad: UUID = UUID.randomUUID(),
        val member: UUID = UUID.randomUUID()
) : Serializable