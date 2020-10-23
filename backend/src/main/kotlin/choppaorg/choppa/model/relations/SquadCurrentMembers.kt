package choppaorg.choppa.model.relations

import choppaorg.choppa.model.Member
import choppaorg.choppa.model.Squad
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable
import java.sql.Timestamp
import java.sql.Timestamp.from
import java.time.Instant.now
import java.util.*
import javax.persistence.*

@Entity
@IdClass(SquadCurrentMembersId::class)
@Table(name = "squad_current_members")
data class SquadCurrentMembers(
        @Id
        @ManyToOne
        @JoinColumn(name = "squad_id", referencedColumnName = "squad_id", columnDefinition = "uuid")
        @JsonProperty("squad")
        val squad: Squad,

        @Id
        @ManyToOne
        @JoinColumn(name = "mem_id", referencedColumnName = "mem_id", columnDefinition = "uuid")
        @JsonProperty("member")
        val member: Member,

        @Column(name = "rotation_date")
        @JsonProperty("rotationDate")
        val rotationDate: Timestamp = from(now())
) {
        override fun toString() = "SquadCurrentMembers(squad=$squad, member=$member)"
}

class SquadCurrentMembersId(
        val squad: UUID = UUID.randomUUID(),
        val member: UUID = UUID.randomUUID()
) : Serializable