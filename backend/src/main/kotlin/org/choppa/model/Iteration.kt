package org.choppa.model

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import org.hibernate.annotations.GenericGenerator
import java.sql.Timestamp
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "iteration")
data class Iteration @JsonCreator constructor(
    @Id
    @Column(name = "iteration_id", columnDefinition = "uuid")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @JsonProperty("id")
    val id: UUID,

    @Column(name = "number", columnDefinition = "INTEGER", nullable = false)
    @JsonProperty("number")
    val number: Int,

    @Column(name = "timebox", columnDefinition = "INTEGER", nullable = false)
    @JsonProperty("timebox")
    val timebox: Int,

    @Column(name = "date", columnDefinition = "TIMESTAMP", nullable = false)
    @JsonProperty("date")
    val date: Timestamp
) {
    override fun toString() = "Iteration(id=$id, number=$number, timebox=$timebox, date=$date)"
}
