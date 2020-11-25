package org.choppa.domain.member

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import org.choppa.domain.chapter.Chapter
import org.choppa.domain.chapter.Chapter.Companion.UNASSIGNED_ROLE
import org.choppa.domain.history.History
import org.choppa.domain.squad.Squad
import org.hibernate.annotations.GenericGenerator
import java.util.UUID
import java.util.UUID.randomUUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToMany
import javax.persistence.ManyToOne
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "member")
@JsonSerialize(using = MemberSerializer::class)
@JsonDeserialize(using = MemberDeserializer::class)
@Suppress("EqualsOrHashCode")
data class Member @JsonCreator constructor(
    @Id
    @Column(name = "member_id", columnDefinition = "uuid")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @JsonProperty("id")
    val id: UUID = randomUUID(),

    @Column(name = "name", columnDefinition = "VARCHAR(100)", nullable = false)
    @JsonProperty("name")
    val name: String = "ME-$id".substring(0, 15),

    @ManyToOne
    @JsonProperty("chapter")
    @JoinColumn(name = "chapter", referencedColumnName = "chapter_id")
    val chapter: Chapter = UNASSIGNED_ROLE,

    @ManyToMany(mappedBy = "members")
    @JsonIgnore
    val squads: MutableList<Squad> = mutableListOf(),

    @OneToMany(mappedBy = "member")
    @JsonIgnore
    val history: MutableList<History> = mutableListOf()
) {
    override fun toString() = "Member(id=$id, name=$name, chapter=$chapter)"
    override fun hashCode(): Int = id.hashCode()

    companion object {
        val NO_MEMBERS get() = mutableListOf<Member>()
    }
}
