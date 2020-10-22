package choppaorg.choppa.model

import choppaorg.choppa.model.ids.SquadMemberId
import java.time.LocalDateTime
import javax.persistence.*

@Entity(name = "squad_current_members")
@IdClass(SquadMemberId::class)
class SquadMember(

    @Id
    @ManyToOne
    @JoinColumn(name = "squad_id", referencedColumnName = "id")
    var squad: Squad,

    @Id
    @ManyToOne
    @JoinColumn(name = "member_id", referencedColumnName = "id")
    var member: Member,

    @Column(name = "rotation_date")
    var rotationDate: LocalDateTime
) {
}

