package org.choppa.repository.relations

import org.choppa.model.relations.SquadCurrentMembers
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface SquadCurrentMembersRepository : JpaRepository<SquadCurrentMembers, UUID>
