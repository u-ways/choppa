package org.choppa.domain.chapter

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import org.choppa.domain.member.Member
import org.choppa.utils.Color.Companion.GREY
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
@JsonSerialize(using = ChapterSerializer::class)
@JsonDeserialize(using = ChapterDeserializer::class)
data class Chapter @JsonCreator constructor(
    @Id
    @Column(name = "chapter_id", columnDefinition = "uuid")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @JsonProperty("id")
    val id: UUID = randomUUID(),

    @Column(name = "name", columnDefinition = "VARCHAR(100)", unique = true, nullable = false)
    @JsonProperty("name")
    val name: String = "CH-$id".substring(0, 15),

    @Column(name = "color", columnDefinition = "INTEGER")
    @JsonProperty("color")
    val color: Int = GREY,

    @OneToMany(mappedBy = "chapter")
    @JsonIgnore
    val members: MutableList<Member> = mutableListOf(),
) {
    override fun toString() = "Chapter(id=$id, name=$name)"

    companion object {
        val UNASSIGNED_ROLE = Chapter(UUID.fromString("00000000-0000-0000-0000-000000000000"), "Unassigned")
    }
}
