package org.choppa.repository

import org.choppa.model.Squad
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface SquadRepository : JpaRepository<Squad, UUID>
