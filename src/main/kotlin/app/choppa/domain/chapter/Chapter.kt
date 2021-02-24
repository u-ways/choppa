package app.choppa.domain.chapter

import app.choppa.domain.account.Account
import app.choppa.domain.base.BaseModel
import app.choppa.domain.member.Member
import app.choppa.utils.Color.Companion.GREY
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import org.hibernate.annotations.GenericGenerator
import java.util.*
import java.util.UUID.fromString
import java.util.UUID.randomUUID
import javax.persistence.*

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

    @ManyToOne
    @JoinColumn(name = "account_id", referencedColumnName = "account_id")
    override val account: Account = Account.DEMO_ACCOUNT,
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
        val UNASSIGNED_ROLE = Chapter(fromString("00000000-0000-0000-0000-000000000000"), "UNASSIGNED")
    }
}
