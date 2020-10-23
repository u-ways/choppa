package choppaorg.choppa.model

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import org.hibernate.annotations.GenericGenerator
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "chapter")
data class Chapter @JsonCreator constructor(
        @Id
        @Column(name = "chap_id", columnDefinition = "uuid")
        @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
        @JsonProperty("id")
        val id: UUID,

        @Column(name = "chap_role")
        @JsonProperty("role")
        val role: String,

        @OneToMany(mappedBy = "chapter")
        @JsonIgnore
        val member: List<Member>
) {
        override fun toString() = "Chapter(id=$id, role=$role)"
}