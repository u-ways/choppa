package org.choppa.repository

import org.choppa.model.Iteration
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface IterationRepository : JpaRepository<Iteration, UUID>
