package org.choppa.model

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import org.choppa.model.relations.IterationHistory
import org.choppa.model.relations.SquadCurrentMembers
import org.hibernate.annotations.GenericGenerator
import java.util.UUID
import java.util.UUID.randomUUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType.EAGER
import javax.persistence.FetchType.LAZY
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "member")
data class Member @JsonCreator constructor(
    @Id
    @Column(name = "member_id", columnDefinition = "uuid")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @JsonProperty("id")
    val id: UUID = randomUUID(),

    @Column(name = "name", columnDefinition = "VARCHAR(100)", nullable = false)
    @JsonProperty("name")
    val name: String,

    @ManyToOne(fetch = EAGER)
    @JsonProperty("chapter")
    @JoinColumn(name = "chapter", referencedColumnName = "chapter_id")
    val chapter: Chapter,

    @OneToMany(mappedBy = "member", fetch = EAGER)
    @JsonIgnore
    var squads: List<SquadCurrentMembers> = emptyList(),

    @OneToMany(mappedBy = "member", fetch = LAZY)
    @JsonIgnore
    var iterations: List<IterationHistory> = emptyList()
) {
    override fun toString() = "Member(id=$id, name=$name, chapter=$chapter)"
}
