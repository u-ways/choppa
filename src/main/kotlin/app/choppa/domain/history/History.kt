package app.choppa.domain.history

import app.choppa.domain.chapter.Chapter
import app.choppa.domain.iteration.Iteration
import app.choppa.domain.member.Member
import app.choppa.domain.squad.Squad
import app.choppa.domain.tribe.Tribe
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import java.time.Instant
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.IdClass
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@IdClass(HistoryId::class)
@Table(name = "history")
@JsonSerialize(using = HistorySerializer::class)
data class History(
    @Id
    @ManyToOne
    @JoinColumn(name = "iteration_id", referencedColumnName = "iteration_id", columnDefinition = "uuid")
    val iteration: Iteration? = null,

    @Id
    @ManyToOne
    @JoinColumn(name = "tribe_id", referencedColumnName = "tribe_id", columnDefinition = "uuid")
    val tribe: Tribe? = null,

    @Id
    @ManyToOne
    @JoinColumn(name = "squad_id", referencedColumnName = "squad_id", columnDefinition = "uuid")
    val squad: Squad? = null,

    @Id
    @ManyToOne
    @JoinColumn(name = "member_id", referencedColumnName = "member_id", columnDefinition = "uuid")
    val member: Member? = null,

    @Id
    @ManyToOne
    @JoinColumn(name = "chapter_id", referencedColumnName = "chapter_id", columnDefinition = "uuid")
    val chapter: Chapter? = null,

    @Id
    @Column(name = "create_date", columnDefinition = "TIMESTAMP WITH TIME ZONE", nullable = false)
    val createDate: Instant = Instant.now()
) {
    override fun toString() =
        "History(iteration=${iteration?.number}, tribe=${tribe?.name}, squad=${squad?.name}, member=${member?.name})"

    override fun hashCode(): Int = HistoryId(iteration?.id, tribe?.id, squad?.id, member?.id, chapter?.id, createDate).hashCode()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as History
        val id = HistoryId(iteration?.id, tribe?.id, squad?.id, member?.id, chapter?.id, createDate).hashCode()
        val otherId =
            HistoryId(
                other.iteration?.id,
                other.tribe?.id,
                other.squad?.id,
                other.member?.id,
                other.chapter?.id,
                other.createDate
            ).hashCode()
        if (id != otherId) return false
        return true
    }
}
