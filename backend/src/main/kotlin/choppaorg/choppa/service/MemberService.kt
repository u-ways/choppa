package choppaorg.choppa.service

import choppaorg.choppa.model.Chapter
import choppaorg.choppa.model.Member
import choppaorg.choppa.repository.MemberRepository
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class MemberService(
    private val memberRepository: MemberRepository
) {

    @Transactional
    fun create(name: String, chapter: Chapter): Member {
        return memberRepository.save(Member(name, chapter));
    }

}