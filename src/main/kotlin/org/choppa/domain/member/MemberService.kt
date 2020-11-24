package org.choppa.domain.member

import org.choppa.domain.base.BaseService
import org.choppa.domain.chapter.ChapterService
import org.choppa.exception.EntityNotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class MemberService(
    @Autowired private val memberRepository: MemberRepository,
    @Autowired private val chapterService: ChapterService
) : BaseService() {
    fun find(id: UUID): Member = memberRepository
        .findById(id)
        .orElseThrow { throw EntityNotFoundException("Member with id [$id] does not exist.") }

    fun find(): List<Member> = memberRepository
        .findAll()
        .orElseThrow { throw EntityNotFoundException("No members exist yet.") }

    fun findRelatedByChapter(chapterId: UUID): List<Member> = memberRepository
        .findAllByChapterId(chapterId)
        .orElseThrow { throw EntityNotFoundException("No members joined chapter [$chapterId] yet.") }

    fun findRelatedBySquad(squadId: UUID): List<Member> = memberRepository
        .findAllBySquadId(squadId)
        .orElseThrow { throw EntityNotFoundException("No members joined squad [$squadId] yet.") }

    fun findRelatedByTribe(tribeId: UUID): List<Member> = memberRepository
        .findAllByTribeId(tribeId)
        .orElseThrow { throw EntityNotFoundException("No members joined tribe [$tribeId] yet.") }

    // NOTE(u-ways) #57 This is used to find all related members by squad for validation purposes
    //                  So it should allow the return of empty lists (i.e. squad w/ NO_MEMBERS)
    fun find(ids: List<UUID>): List<Member> = memberRepository
        .findAllById(ids)

    // NOTE(u-ways) #57 Member is the owning map of chapter.
    //                  Therefore, service ensure chapter exist before relating chapter accordingly.
    //                  This avoids invalid foreign key inserts.
    @Transactional
    fun save(member: Member): Member {
        val chapter = chapterService.find(member.chapter.id)
        return memberRepository.save(Member(member.id, member.name, chapter))
    }

    @Transactional
    fun save(members: List<Member>): List<Member> {
        return members.map(::save)
    }

    @Transactional
    fun save(vararg members: Member): List<Member> {
        return save(members.toMutableList())
    }

    fun delete(member: Member): Member {
        memberRepository.delete(member)
        return member
    }

    fun delete(members: List<Member>): List<Member> {
        memberRepository.deleteAll(members)
        return members
    }

    fun delete(vararg members: Member): List<Member> {
        return delete(members.toMutableList())
    }
}
