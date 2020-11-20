package org.choppa.repository

import org.choppa.model.tribe.Tribe
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface TribeRepository : JpaRepository<Tribe, UUID>
