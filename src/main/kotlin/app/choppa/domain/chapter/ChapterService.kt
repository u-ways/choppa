package app.choppa.domain.chapter

import app.choppa.domain.account.Account
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
    override fun find(account: Account): List<Chapter> = chapterRepository
        .findAll()
        .ownedBy(account)
        .orElseThrow { throw EntityNotFoundException("No chapters exist yet.") }

    override fun find(id: UUID, account: Account): Chapter = chapterRepository
        .findById(id)
        .orElseThrow { throw EntityNotFoundException("Chapter with id [$id] does not exist.") }
        .verifyOwnership(account)

    override fun find(ids: List<UUID>, account: Account): List<Chapter> = chapterRepository
        .findAllById(ids)
        .ownedBy(account)
        .orElseThrow { throw EntityNotFoundException("No chapters found with given ids.") }

    override fun save(entity: Chapter, account: Account): Chapter = chapterRepository.save(
        chapterRepository
            .findById(entity.id)
            .verifyOriginalOwnership(entity, account)
    )

    override fun save(entities: List<Chapter>, account: Account): List<Chapter> = entities
        .map { this.save(it, account) }

    override fun delete(entity: Chapter, account: Account): Chapter = chapterRepository.findById(entity.id).run {
        this.orElseGet { throw EntityNotFoundException("Chapter with id [${entity.id}] does not exist.") }
            .verifyOwnership(account).also {
                memberService.unAssignMembersWithChapter(entity, account)
                chapterRepository.delete(entity)
            }
    }

    override fun delete(entities: List<Chapter>, account: Account): List<Chapter> = entities
        .map { delete(it, account) }

    fun findRelatedBySquad(squadId: UUID, account: Account): List<Chapter> = chapterRepository
        .findAllBySquadId(squadId)
        .ownedBy(account)
        .orElseThrow { throw EntityNotFoundException("No chapters in squad [$squadId] exist yet.") }

    fun findRelatedByTribe(tribeId: UUID, account: Account): List<Chapter> = chapterRepository
        .findAllByTribeId(tribeId)
        .ownedBy(account)
        .orElseThrow { throw EntityNotFoundException("No chapters in tribe [$tribeId] exist yet.") }

    fun statistics(account: Account): Map<String, Serializable> = chapterRepository.findAll().ownedBy(account).run {
        val members = memberService.runCatching { this.find(account) }.getOrElse { emptyList() }
        mapOf(
            "total" to this.size,
            "distribution" to this.fold(HashMap<String, Double>(this.size)) { acc, chapter ->
                acc.also {
                    acc[chapter.name] = members.count { it.chapter.name == chapter.name }.toDouble().div(members.size).round()
                }
            }
        )
    }

    private fun Optional<Chapter>.verifyOriginalOwnership(entity: Chapter, account: Account): Chapter =
        if (this.isPresent) entity.copy(account = this.get().account).verifyOwnership(account)
        else entity.copy(account = account)
}
