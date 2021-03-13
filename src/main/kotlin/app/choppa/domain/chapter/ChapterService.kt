package app.choppa.domain.chapter

import app.choppa.domain.account.AccountService
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
    @Autowired private val memberService: MemberService,
    @Autowired private val accountService: AccountService,
) : BaseService<Chapter>(accountService) {
    override fun find(): List<Chapter> = chapterRepository
        .findAll()
        .ownedByAuthenticated()
        .orElseThrow { throw EntityNotFoundException("No chapters exist yet.") }

    override fun find(id: UUID): Chapter = chapterRepository
        .findById(id)
        .orElseThrow { throw EntityNotFoundException("Chapter with id [$id] does not exist.") }
        .verifyAuthenticatedOwnership()

    override fun find(ids: List<UUID>): List<Chapter> = chapterRepository
        .findAllById(ids)
        .ownedByAuthenticated()
        .orElseThrow { throw EntityNotFoundException("No chapters found with given ids.") }

    override fun save(entity: Chapter): Chapter = chapterRepository.save(
        chapterRepository
            .findById(entity.id)
            .verifyOriginalOwnership(entity)
    )

    override fun save(entities: List<Chapter>): List<Chapter> = entities
        .map { this.save(it) }

    override fun delete(entity: Chapter): Chapter = chapterRepository.findById(entity.id).run {
        this.orElseGet { throw EntityNotFoundException("Chapter with id [${entity.id}] does not exist.") }
            .verifyAuthenticatedOwnership().also {
                memberService.unAssignMembersWithChapter(entity)
                chapterRepository.delete(entity)
            }
    }

    override fun delete(entities: List<Chapter>): List<Chapter> = entities
        .map { delete(it) }

    fun findRelatedBySquad(squadId: UUID): List<Chapter> = chapterRepository
        .findAllBySquadId(squadId)
        .ownedByAuthenticated()
        .orElseThrow { throw EntityNotFoundException("No chapters in squad [$squadId] exist yet.") }

    fun findRelatedByTribe(tribeId: UUID): List<Chapter> = chapterRepository
        .findAllByTribeId(tribeId)
        .ownedByAuthenticated()
        .orElseThrow { throw EntityNotFoundException("No chapters in tribe [$tribeId] exist yet.") }

    fun statistics(): Map<String, Serializable> = chapterRepository.findAll().ownedByAuthenticated().run {
        val members = memberService.runCatching { this.find() }.getOrElse { emptyList() }
        mapOf(
            "total" to this.size,
            "distribution" to this.fold(HashMap<String, Double>(this.size)) { acc, chapter ->
                acc.also {
                    acc[chapter.name] =
                        members.count { it.chapter.name == chapter.name }.toDouble().div(members.size).round()
                }
            }
        )
    }

    private fun Optional<Chapter>.verifyOriginalOwnership(entity: Chapter): Chapter =
        if (this.isPresent) entity.copy(account = this.get().account).verifyAuthenticatedOwnership()
        else entity.copy(account = accountService.resolveFromAuth())
}
