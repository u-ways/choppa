package org.choppa.model.history

import com.fasterxml.jackson.annotation.JsonProperty
import org.choppa.model.iteration.Iteration
import org.choppa.model.member.Member
import org.choppa.model.squad.Squad
import org.choppa.model.tribe.Tribe
import javax.persistence.Entity
import javax.persistence.FetchType.EAGER
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
    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "iteration_id", referencedColumnName = "iteration_id", columnDefinition = "uuid")
    @JsonProperty("iteration")
    val iteration: Iteration,

    @Id
    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "tribe_id", referencedColumnName = "tribe_id", columnDefinition = "uuid")
    @JsonProperty("tribe")
    val tribe: Tribe,

    @Id
    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "squad_id", referencedColumnName = "squad_id", columnDefinition = "uuid")
    @JsonProperty("squad")
    val squad: Squad,

    @Id
    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "member_id", referencedColumnName = "member_id", columnDefinition = "uuid")
    @JsonProperty("member")
    val member: Member
) {
    override fun toString() = "IterationHistory(iteration=$iteration, tribe=$tribe, squad=$squad, member=$member)"
}
