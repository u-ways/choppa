package org.choppa.repository.relations

import org.choppa.model.relations.SquadCurrentMembers
import org.choppa.model.relations.SquadCurrentMembersId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SquadCurrentMembersRepository : JpaRepository<SquadCurrentMembers, SquadCurrentMembersId>
