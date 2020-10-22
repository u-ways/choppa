package choppaorg.choppa.model

import javax.persistence.*

@Entity
class Squad(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int?,

    var name: String,

    @OneToMany(cascade = [ CascadeType.ALL ], mappedBy = "squad")
    var members: Set<SquadMember>

) {
    constructor(name: String): this(null, name, emptySet())
}

