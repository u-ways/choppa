package app.choppa.domain.member

import app.choppa.domain.base.BaseService
import app.choppa.exception.EntityNotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class MemberService(
    @Autowired private val memberRepository: MemberRepository
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

    @Transactional
    override fun save(entity: Member): Member = memberRepository.save(entity)

    @Transactional
    override fun save(entities: List<Member>): List<Member> = entities.map(::save)

    override fun delete(entities: List<Member>): List<Member> = entities
        .apply { memberRepository.deleteAll(entities) }

    override fun delete(entity: Member): Member = entity
        .apply { memberRepository.delete(entity) }

    fun findRelatedByChapter(chapterId: UUID): List<Member> = memberRepository
        .findAllByChapterId(chapterId)
        .orElseThrow { throw EntityNotFoundException("No members joined chapter [$chapterId] yet.") }

    fun findRelatedBySquad(squadId: UUID): List<Member> = memberRepository
        .findAllBySquadId(squadId)
        .orElseThrow { throw EntityNotFoundException("No members joined squad [$squadId] yet.") }

    fun findRelatedByTribe(tribeId: UUID): List<Member> = memberRepository
        .findAllByTribeId(tribeId)
        .orElseThrow { throw EntityNotFoundException("No members joined tribe [$tribeId] yet.") }
}
