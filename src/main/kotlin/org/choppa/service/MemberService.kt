package org.choppa.service

import org.choppa.helpers.exception.EmptyListException
import org.choppa.helpers.exception.EntityNotFoundException
import org.choppa.model.member.Member
import org.choppa.repository.MemberRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class MemberService(
    @Autowired private val memberRepository: MemberRepository,
    @Autowired private val chapterService: ChapterService
) {
    fun find(id: UUID): Member? {
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

    // NOTE(u-ways) #57 member is the owning map of chapter, it should update chapter correctly.
    //                  So we need to consider deserialization on save.
    @Transactional
    fun save(member: Member): Member {
        val chapter = chapterService.find(member.chapter.id)
        val member2 = Member(member.id, member.name, chapter)
        return memberRepository.save(member2)
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
