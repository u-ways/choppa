package choppaorg.choppa.repository

import choppaorg.choppa.model.Member
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface MemberRepository : JpaRepository<Member, UUID>