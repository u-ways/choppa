package app.choppa.domain.chapter

import app.choppa.domain.base.BaseModel
import app.choppa.domain.history.History
import app.choppa.domain.member.Member
import app.choppa.utils.Color.Companion.GREY
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import org.hibernate.annotations.GenericGenerator
import java.util.UUID
import java.util.UUID.fromString
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
data class Chapter(
    @Id
    @Column(name = "chapter_id", columnDefinition = "uuid")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    override val id: UUID = randomUUID(),

    @Column(name = "name", columnDefinition = "VARCHAR(100)", unique = true, nullable = false)
    val name: String = "CH-$id".substring(0, 15),

    @Column(name = "color", columnDefinition = "INTEGER")
    val color: Int = GREY,

    @OneToMany(mappedBy = "chapter")
    val members: MutableList<Member> = mutableListOf(),

    @OneToMany(mappedBy = "chapter")
    val history: MutableList<History> = mutableListOf()
) : BaseModel {
    override fun toString() = "Chapter(id=$id, name=$name)"

    override fun hashCode(): Int = id.hashCode()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Chapter
        if (id != other.id) return false
        return true
    }

    companion object {
        val UNASSIGNED_ROLE = Chapter(fromString("00000000-0000-0000-0000-000000000000"), "Unassigned")
    }
}
