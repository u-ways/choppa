package org.choppa.domain.history

import com.fasterxml.jackson.annotation.JsonProperty
import org.choppa.domain.iteration.Iteration
import org.choppa.domain.member.Member
import org.choppa.domain.squad.Squad
import org.choppa.domain.tribe.Tribe
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.IdClass
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@IdClass(HistoryId::class)
@Table(name = "history")
data class History(
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
