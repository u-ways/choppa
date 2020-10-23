package choppaorg.choppa.service

import choppaorg.choppa.model.Member
import choppaorg.choppa.repository.MemberRepository
import choppaorg.choppa.service.relations.IterationHistoryService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class MemberService(
        @Autowired private val memberRepository: MemberRepository,
        @Autowired private val chapterService: ChapterService,
        @Autowired private val iterationHistoryService: IterationHistoryService
) {
    fun find(id: UUID): Member? {
        return memberRepository.findById(id).get()
    }

    fun find(): List<Member> {
        return memberRepository.findAll()
    }

    fun find(ids: List<UUID>): List<Member> {
        return memberRepository.findAllById(ids)
    }

    @Transactional
    fun save(member: Member): Member {
        chapterService.save(member.chapter)
        iterationHistoryService.save(member.iterations)
        return memberRepository.save(member)
    }

    @Transactional
    fun save(members: List<Member>): List<Member> {
        chapterService.save(members.map { it.chapter })
        members.forEach { iterationHistoryService.save(it.iterations) }
        return memberRepository.saveAll(members)
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
