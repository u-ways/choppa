package choppaorg.choppa.model

import javax.persistence.*

@Entity
class Member(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int?,

    var name: String,

    @OneToOne
    @JoinColumn(name = "chapter_id")
    var chapter: Chapter?
) {
    constructor(name: String, chapter: Chapter): this(null, name, chapter)
}