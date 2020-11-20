package org.choppa.model.iteration

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import org.hibernate.annotations.GenericGenerator
import java.time.Instant
import java.time.Instant.now
import java.time.temporal.ChronoUnit.DAYS
import java.util.UUID
import java.util.UUID.randomUUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table
import kotlin.Int.Companion.MAX_VALUE

@Entity
@Table(name = "iteration")
@JsonSerialize(using = Serializer::class)
@JsonDeserialize(using = Deserializer::class)
data class Iteration @JsonCreator constructor(
    @Id
    @Column(name = "iteration_id", columnDefinition = "uuid")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @JsonProperty("id")
    val id: UUID = randomUUID(),

    @Column(name = "number", columnDefinition = "INTEGER", nullable = false)
    @JsonProperty("number")
    val number: Int = (1..MAX_VALUE).random(),

    @Column(name = "start_date", columnDefinition = "TIMESTAMP WITH TIME ZONE", nullable = false)
    @JsonProperty("startDate")
    val startDate: Instant = now(),

    @Column(name = "end_date", columnDefinition = "TIMESTAMP WITH TIME ZONE", nullable = false)
    @JsonProperty("endDate")
    val endDate: Instant = startDate.plus(14, DAYS)
) {
    override fun toString() = "Iteration(id=$id, number=$number, startDate=$startDate, endDate=$endDate)"
}
