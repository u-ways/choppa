package org.choppa.repository.relations

import org.choppa.model.relations.IterationHistory
import org.choppa.model.relations.IterationHistoryId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface IterationHistoryRepository : JpaRepository<IterationHistory, IterationHistoryId>
