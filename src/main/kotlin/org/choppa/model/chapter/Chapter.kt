package org.choppa.model.chapter

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import org.choppa.model.Member
import org.hibernate.annotations.GenericGenerator
import java.util.UUID
import java.util.UUID.randomUUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "chapter")
@JsonSerialize(using = Serializer::class)
@JsonDeserialize(using = Deserializer::class)
data class Chapter @JsonCreator constructor(
    @Id
    @Column(name = "chapter_id", columnDefinition = "uuid")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @JsonProperty("id")
    val id: UUID = randomUUID(),

    @Column(name = "name", columnDefinition = "VARCHAR(100)", unique = true, nullable = false)
    @JsonProperty("name")
    val name: String,

    @OneToMany(mappedBy = "chapter")
    @JsonIgnore
    val members: List<Member> = emptyList(),
) {
    override fun toString() = "Chapter(id=$id, name=$name)"
}
