package app.choppa.domain.chapter

import app.choppa.domain.base.BaseService
import app.choppa.domain.member.MemberService
import app.choppa.exception.EntityNotFoundException
import app.choppa.utils.Numbers.Companion.round
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.io.Serializable
import java.util.*
import kotlin.collections.HashMap

@Service
class ChapterService(
    @Autowired private val chapterRepository: ChapterRepository,
    @Autowired private val memberService: MemberService
) : BaseService<Chapter> {
    override fun find(): List<Chapter> = chapterRepository
        .findAll()
        .orElseThrow { throw EntityNotFoundException("No chapters exist yet.") }

    override fun find(id: UUID): Chapter = chapterRepository
        .findById(id)
        .orElseThrow { throw EntityNotFoundException("Chapter with id [$id] does not exist.") }

    override fun find(ids: List<UUID>): List<Chapter> = chapterRepository
        .findAllById(ids)
        .orElseThrow { throw EntityNotFoundException("No chapters found with given ids.") }

    override fun save(entity: Chapter): Chapter = chapterRepository
        .save(entity)

    override fun save(entities: List<Chapter>): List<Chapter> = chapterRepository
        .saveAll(entities)

    override fun delete(entity: Chapter): Chapter = entity.apply {
        // convert all related members to UNASSIGNED_ROLE
        memberService.unAssignMembersWithChapter(entity)
        chapterRepository.delete(entity)
    }

    override fun delete(entities: List<Chapter>): List<Chapter> = entities.map(::delete)

    fun findRelatedBySquad(squadId: UUID): List<Chapter> = chapterRepository
        .findAllBySquadId(squadId)
        .orElseThrow { throw EntityNotFoundException("No chapters in squad [$squadId] exist yet.") }

    fun findRelatedByTribe(tribeId: UUID): List<Chapter> = chapterRepository
        .findAllByTribeId(tribeId)
        .orElseThrow { throw EntityNotFoundException("No chapters in tribe [$tribeId] exist yet.") }

    fun statistics(): Map<String, Serializable> = chapterRepository.findAll().run {
        val members = memberService.runCatching { this.find() }.getOrElse { emptyList() }
        mapOf(
            "Total" to this.size,
            "Distribution" to this.fold(HashMap<String, Double>(this.size)) { acc, chapter ->
                acc.also {
                    acc[chapter.name] = members.count { it.chapter.name == chapter.name }.toDouble().div(members.size).round()
                }
            }
        )
    }
}
