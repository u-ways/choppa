package org.choppa.domain.member

import org.choppa.domain.chapter.ChapterService
import org.choppa.exception.EmptyListException
import org.choppa.exception.EntityNotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class MemberService(
    @Autowired private val memberRepository: MemberRepository,
    @Autowired private val chapterService: ChapterService
) {
    fun find(id: UUID): Member {
        return memberRepository.findById(id).orElseThrow {
            throw EntityNotFoundException("Member with id [$id] does not exist.")
        }
    }

    fun find(): List<Member> {
        val members = memberRepository.findAll()
        return if (members.isEmpty()) throw EmptyListException("No members exist yet.") else members
    }

    fun find(ids: List<UUID>): List<Member> {
        return memberRepository.findAllById(ids)
    }

    // NOTE(u-ways) #57 Member is the owning map of chapter.
    //                  Therefore, service ensure they exist before relating chapter accordingly.
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
