package org.choppa.model.relations

import com.fasterxml.jackson.annotation.JsonProperty
import org.choppa.model.Member
import org.choppa.model.Squad
import java.io.Serializable
import java.time.Instant
import java.time.Instant.now
import java.util.UUID
import java.util.UUID.randomUUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType.EAGER
import javax.persistence.Id
import javax.persistence.IdClass
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@IdClass(SquadCurrentMembersId::class)
@Table(name = "squad_current_members")
data class SquadCurrentMembers(
    @Id
    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "squad_id", referencedColumnName = "squad_id", columnDefinition = "uuid")
    @JsonProperty("squad")
    val squad: Squad,

    @Id
    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "member_id", referencedColumnName = "member_id", columnDefinition = "uuid")
    @JsonProperty("member")
    val member: Member,

    @Column(name = "rotation_date", columnDefinition = "TIMESTAMP", nullable = false)
    @JsonProperty("rotationDate")
    val rotationDate: Instant = now()
) {
    override fun toString() = "SquadCurrentMembers(squad=$squad, member=$member, rotationDate=$rotationDate)"
}

class SquadCurrentMembersId(
    val squad: UUID = randomUUID(),
    val member: UUID = randomUUID()
) : Serializable
