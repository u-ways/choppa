package org.choppa.model.relations

import com.fasterxml.jackson.annotation.JsonProperty
import org.choppa.model.Iteration
import org.choppa.model.Member
import org.choppa.model.Squad
import org.choppa.model.Tribe
import java.io.Serializable
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.IdClass
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

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
