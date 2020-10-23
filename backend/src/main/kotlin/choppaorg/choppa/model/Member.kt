package choppaorg.choppa.model

import choppaorg.choppa.model.relations.IterationHistory
import choppaorg.choppa.model.relations.SquadCurrentMembers
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import org.hibernate.annotations.GenericGenerator
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "member")
data class Member @JsonCreator constructor(
        @Id
        @Column(name = "member_id", columnDefinition = "uuid")
        @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
        @JsonProperty("id")
        val id: UUID,

        @Column(name = "name", columnDefinition = "VARCHAR(100)", nullable = false)
        @JsonProperty("name")
        val name: String,

        @ManyToOne(fetch = FetchType.LAZY)
        @JsonProperty("chapter")
        @JoinColumn(name = "chapter", referencedColumnName = "chapter_id")
        val chapter: Chapter,

        @OneToMany(mappedBy = "member")
        @JsonIgnore
        var squads: MutableList<SquadCurrentMembers> = mutableListOf(),

        @OneToMany(mappedBy = "member")
        @JsonIgnore
        var iterations: MutableList<IterationHistory> = mutableListOf()
) {
        override fun toString() = "Member(id=$id, name=$name, chapter=$chapter)"
}