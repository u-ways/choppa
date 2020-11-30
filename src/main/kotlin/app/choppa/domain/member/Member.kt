package app.choppa.domain.member

import app.choppa.domain.base.BaseModel
import app.choppa.domain.chapter.Chapter
import app.choppa.domain.chapter.Chapter.Companion.UNASSIGNED_ROLE
import app.choppa.domain.history.History
import app.choppa.domain.squad.Squad
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
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
data class Member(
    @Id
    @Column(name = "member_id", columnDefinition = "uuid")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    override val id: UUID = randomUUID(),

    @Column(name = "name", columnDefinition = "VARCHAR(100)", nullable = false)
    val name: String = "ME-$id".substring(0, 15),

    @ManyToOne
    @JoinColumn(name = "chapter", referencedColumnName = "chapter_id")
    val chapter: Chapter = UNASSIGNED_ROLE,

    @ManyToMany(mappedBy = "members")
    val squads: MutableList<Squad> = mutableListOf(),

    @OneToMany(mappedBy = "member")
    val history: MutableList<History> = mutableListOf()
) : BaseModel {
    override fun toString() = "Member(id=$id, name=$name, chapter=$chapter)"

    override fun hashCode(): Int = id.hashCode()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Member
        if (id != other.id) return false
        return true
    }

    companion object {
        val NO_MEMBERS get() = mutableListOf<Member>()
    }
}
