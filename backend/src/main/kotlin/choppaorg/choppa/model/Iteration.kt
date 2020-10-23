package choppaorg.choppa.model

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import org.hibernate.annotations.GenericGenerator
import java.sql.Timestamp
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "iteration")
data class Iteration @JsonCreator constructor(
        @Id
        @Column(name = "iter_id", columnDefinition = "uuid")
        @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
        @JsonProperty("id")
        val id: UUID,

        @Column(name = "iter_no")
        @JsonProperty("number")
        val number: Int,

        @Column(name = "iter_timebox")
        @JsonProperty("timebox")
        val timebox: Int,

        @Column(name = "iter_date")
        @JsonProperty("date")
        val date: Timestamp
) {
        override fun toString() = "Iteration(id=$id, number=$number, timebox=$timebox, date=$date)"
}