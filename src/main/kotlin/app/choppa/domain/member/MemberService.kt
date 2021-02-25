package app.choppa.domain.member

import app.choppa.domain.account.Account
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
) : BaseService<Member> {
    override fun find(account: Account): List<Member> = memberRepository
        .findAll()
        .ownedBy(account)
        .orElseThrow { throw EntityNotFoundException("No members exist yet.") }

    override fun find(id: UUID, account: Account): Member = memberRepository
        .findById(id)
        .orElseThrow { throw EntityNotFoundException("Member with id [$id] does not exist.") }
        .verifyOwnership(account)

    override fun find(ids: List<UUID>, account: Account): List<Member> = memberRepository
        .findAllById(ids)
        .ownedBy(account)
        .orElseThrow { throw EntityNotFoundException("No members found.") }

    override fun save(entity: Member, account: Account): Member = memberRepository.save(
        memberRepository
            .findById(entity.id)
            .verifyOriginalOwnership(entity, account)
    )

    override fun save(entities: List<Member>, account: Account): List<Member> = entities
        .map { this.save(it, account) }

    @Transactional
    override fun delete(entity: Member, account: Account): Member = memberRepository.findById(entity.id).run {
        this.orElseGet { throw EntityNotFoundException("Member with id [${entity.id}] does not exist.") }
            .verifyOwnership(account).also {
                squadMemberHistoryService.deleteAllFor(entity)
                memberRepository.deleteAllSquadMemberRecordsFor(entity.id)
                memberRepository.delete(entity)
            }
    }

    @Transactional
    override fun delete(entities: List<Member>, account: Account): List<Member> = entities
        .map { this.delete(it, account) }

    fun findInactive(account: Account): List<Member> = memberRepository
        .findAllByActiveFalse()
        .ownedBy(account)
        .orElseThrow { throw EntityNotFoundException("No inactive members found.") }

    fun findRelatedByChapter(chapterId: UUID, account: Account): List<Member> = memberRepository
        .findAllByChapterId(chapterId)
        .ownedBy(account)
        .orElseThrow { throw EntityNotFoundException("No members joined chapter [$chapterId] yet.") }

    fun findRelatedBySquad(squadId: UUID, account: Account): List<Member> = memberRepository
        .findAllBySquadId(squadId)
        .ownedBy(account)
        .orElseThrow { throw EntityNotFoundException("No members joined squad [$squadId] yet.") }

    fun findRelatedByTribe(tribeId: UUID, account: Account): List<Member> = memberRepository
        .findAllByTribeId(tribeId)
        .ownedBy(account)
        .orElseThrow { throw EntityNotFoundException("No members joined tribe [$tribeId] yet.") }

    @Transactional
    fun unAssignMembersWithChapter(chapter: Chapter, account: Account) = memberRepository
        .findAllByChapterId(chapter.id)
        .ownedBy(account)
        .forEach { save(it.copy(chapter = UNASSIGNED_ROLE), account) }

    fun statistics(account: Account): HashMap<String, Any> = memberRepository.findAll().ownedBy(account).run {
        hashMapOf(
            "total" to this.size,
            "distribution" to mapOf(
                "active" to this.count { it.active }.toDouble().div(if (this.isNotEmpty()) this.size else 1).round(),
                "inactive" to this.count { !it.active }.toDouble().div(if (this.isNotEmpty()) this.size else 1).round(),
            )
        )
    }

    private fun Optional<Member>.verifyOriginalOwnership(entity: Member, account: Account): Member =
        if (this.isPresent) entity.copy(account = this.get().account).verifyOwnership(account)
        else entity.copy(account = account)
}
