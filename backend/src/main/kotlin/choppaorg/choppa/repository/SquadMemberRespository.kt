package choppaorg.choppa.repository

import choppaorg.choppa.model.SquadMember
import choppaorg.choppa.model.ids.SquadMemberId
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface SquadMemberRepository : CrudRepository<SquadMember, SquadMemberId>