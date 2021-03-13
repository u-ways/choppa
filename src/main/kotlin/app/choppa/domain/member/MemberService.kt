package app.choppa.domain.member

import app.choppa.domain.account.AccountService
import app.choppa.domain.base.BaseService
import app.choppa.domain.chapter.Chapter
import app.choppa.domain.chapter.Chapter.Companion.UNASSIGNED_ROLE
import app.choppa.domain.squad.history.SquadMemberHistoryService
import app.choppa.exception.EntityNotFoundException
import app.choppa.utils.Numbers.Companion.round
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class MemberService(
    @Autowired private val memberRepository: MemberRepository,
    @Autowired private val squadMemberHistoryService: SquadMemberHistoryService,
    @Autowired private val accountService: AccountService
) : BaseService<Member>(accountService) {

    override fun find(): List<Member> = memberRepository
        .findAll()
        .ownedByAuthenticated()
        .orElseThrow { throw EntityNotFoundException("No members exist yet.") }

    override fun find(id: UUID): Member = memberRepository
        .findById(id)
        .orElseThrow { throw EntityNotFoundException("Member with id [$id] does not exist.") }
        .verifyAuthenticatedOwnership()

    override fun find(ids: List<UUID>): List<Member> = memberRepository
        .findAllById(ids)
        .ownedByAuthenticated()
        .orElseThrow { throw EntityNotFoundException("No members found.") }

    override fun save(entity: Member): Member = memberRepository.save(
        memberRepository
            .findById(entity.id)
            .verifyOriginalOwnership(entity)
    )

    override fun save(entities: List<Member>): List<Member> = entities
        .map { this.save(it) }

    @Transactional
    override fun delete(entity: Member): Member = memberRepository.findById(entity.id).run {
        this.orElseGet { throw EntityNotFoundException("Member with id [${entity.id}] does not exist.") }
            .verifyAuthenticatedOwnership().also {
                squadMemberHistoryService.deleteAllFor(entity)
                memberRepository.deleteAllSquadMemberRecordsFor(entity.id)
                memberRepository.delete(entity)
            }
    }

    @Transactional
    override fun delete(entities: List<Member>): List<Member> = entities
        .map { this.delete(it) }

    fun findInactive(): List<Member> = memberRepository
        .findAllByActiveFalse()
        .ownedByAuthenticated()
        .orElseThrow { throw EntityNotFoundException("No inactive members found.") }

    fun findRelatedByChapter(chapterId: UUID): List<Member> = memberRepository
        .findAllByChapterId(chapterId)
        .ownedByAuthenticated()
        .orElseThrow { throw EntityNotFoundException("No members joined chapter [$chapterId] yet.") }

    fun findRelatedBySquad(squadId: UUID): List<Member> = memberRepository
        .findAllBySquadId(squadId)
        .ownedByAuthenticated()
        .orElseThrow { throw EntityNotFoundException("No members joined squad [$squadId] yet.") }

    fun findRelatedByTribe(tribeId: UUID): List<Member> = memberRepository
        .findAllByTribeId(tribeId)
        .ownedByAuthenticated()
        .orElseThrow { throw EntityNotFoundException("No members joined tribe [$tribeId] yet.") }

    @Transactional
    fun unAssignMembersWithChapter(chapter: Chapter) = memberRepository
        .findAllByChapterId(chapter.id)
        .ownedByAuthenticated()
        .forEach { save(it.copy(chapter = UNASSIGNED_ROLE)) }

    fun statistics(): HashMap<String, Any> = memberRepository.findAll().ownedByAuthenticated().run {
        hashMapOf(
            "total" to this.size,
            "distribution" to mapOf(
                "active" to this.count { it.active }.toDouble().div(if (this.isNotEmpty()) this.size else 1).round(),
                "inactive" to this.count { !it.active }.toDouble().div(if (this.isNotEmpty()) this.size else 1).round(),
            )
        )
    }

    private fun Optional<Member>.verifyOriginalOwnership(entity: Member): Member =
        if (this.isPresent) entity.copy(account = this.get().account).verifyAuthenticatedOwnership()
        else entity.copy(account = accountService.resolveFromAuth())
}
