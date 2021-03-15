package app.choppa.domain.chapter

import app.choppa.domain.account.Account
import app.choppa.domain.account.Account.Companion.PLACEHOLDER_ACCOUNT
import app.choppa.domain.base.BaseModel
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import org.hibernate.annotations.GenericGenerator
import java.util.*
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
    val name: String,

    @Column(name = "color", columnDefinition = "INTEGER")
    val color: Int,

    @ManyToOne
    @JoinColumn(name = "account_id", referencedColumnName = "account_id")
    override val account: Account,
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
        const val UNASSIGNED_ROLE = "UNASSIGNED"
        val PLACEHOLDER_CHAPTER: Chapter
            get() = Chapter(name = "", color = 0, account = PLACEHOLDER_ACCOUNT)
    }
}
