package app.choppa.domain.member

import app.choppa.domain.account.Account
import app.choppa.domain.account.Account.Companion.PLACEHOLDER_ACCOUNT
import app.choppa.domain.base.BaseModel
import app.choppa.domain.chapter.Chapter
import app.choppa.domain.chapter.Chapter.Companion.PLACEHOLDER_CHAPTER
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import org.hibernate.annotations.GenericGenerator
import java.util.*
import java.util.UUID.randomUUID
import javax.persistence.*

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
    val name: String,

    @ManyToOne
    @JoinColumn(name = "chapter", referencedColumnName = "chapter_id")
    val chapter: Chapter,

    @Column(name = "active", columnDefinition = "BOOLEAN")
    val active: Boolean = true,

    @ManyToOne
    @JoinColumn(name = "account_id", referencedColumnName = "account_id")
    override val account: Account,
) : BaseModel {
    override fun toString() = "Member(id=$id, name=$name, chapter=${chapter.name})"

    override fun hashCode(): Int = id.hashCode()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Member
        if (id != other.id) return false
        return true
    }

    companion object {
        val NO_MEMBERS: MutableList<Member>
            get() = mutableListOf()
        val PLACEHOLDER_MEMBER: Member
            get() = Member(name = "", chapter = PLACEHOLDER_CHAPTER, account = PLACEHOLDER_ACCOUNT)
    }
}
