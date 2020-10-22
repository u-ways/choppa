package choppaorg.choppa.model

import javax.persistence.*

@Entity
class Tribe (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int?,

    var name: String,

    @ManyToMany
    @JoinTable(
        name = "tribe_current_squads",
        joinColumns = [JoinColumn(name = "tribe_id")],
        inverseJoinColumns = [JoinColumn(name = "squad_id")]
    )
    var squads: Set<Squad>
) {

}