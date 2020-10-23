package choppaorg.choppa.repository.relations

import choppaorg.choppa.model.relations.SquadCurrentMembers
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface SquadCurrentMembersRepository : JpaRepository<SquadCurrentMembers, UUID>