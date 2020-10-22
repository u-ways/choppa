package choppaorg.choppa.repository

import choppaorg.choppa.model.Member
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface MemberRepository : CrudRepository<Member, Int>