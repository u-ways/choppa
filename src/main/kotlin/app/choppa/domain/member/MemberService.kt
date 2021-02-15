package app.choppa.domain.member

import app.choppa.domain.base.BaseService
import app.choppa.domain.chapter.Chapter
import app.choppa.domain.chapter.Chapter.Companion.UNASSIGNED_ROLE
import app.choppa.domain.squad.history.SquadMemberHistoryService
import app.choppa.exception.EntityNotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class MemberService(
    @Autowired private val memberRepository: MemberRepository,
    @Autowired private val squadMemberHistoryService: SquadMemberHistoryService,
) : BaseService<Member> {
    override fun find(): List<Member> = memberRepository
        .findAll()
        .orElseThrow { throw EntityNotFoundException("No members exist yet.") }

    override fun find(id: UUID): Member = memberRepository
        .findById(id)
        .orElseThrow { throw EntityNotFoundException("Member with id [$id] does not exist.") }

    override fun find(ids: List<UUID>): List<Member> = memberRepository
        .findAllById(ids)
        .orElseThrow { throw EntityNotFoundException("No members found.") }

    override fun save(entity: Member): Member = memberRepository.save(entity)

    override fun save(entities: List<Member>): List<Member> = memberRepository.saveAll(entities)

    @Transactional
    override fun delete(entity: Member): Member = entity.apply {
        squadMemberHistoryService.deleteAllFor(entity)
        memberRepository.deleteAllSquadMemberRecordsFor(entity.id)
        memberRepository.delete(entity)
    }

    @Transactional
    override fun delete(entities: List<Member>): List<Member> = entities.map(::delete)

    fun findRelatedByChapter(chapterId: UUID): List<Member> = memberRepository
        .findAllByChapterId(chapterId)
        .orElseThrow { throw EntityNotFoundException("No members joined chapter [$chapterId] yet.") }

    fun findRelatedBySquad(squadId: UUID): List<Member> = memberRepository
        .findAllBySquadId(squadId)
        .orElseThrow { throw EntityNotFoundException("No members joined squad [$squadId] yet.") }

    fun findRelatedByTribe(tribeId: UUID): List<Member> = memberRepository
        .findAllByTribeId(tribeId)
        .orElseThrow { throw EntityNotFoundException("No members joined tribe [$tribeId] yet.") }

    @Transactional
    fun unAssignMembersWithChapter(chapter: Chapter) = memberRepository
        .findAllByChapterId(chapter.id)
        .forEach { save(it.copy(chapter = UNASSIGNED_ROLE)) }
}
