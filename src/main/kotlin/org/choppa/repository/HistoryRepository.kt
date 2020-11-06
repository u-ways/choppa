package org.choppa.repository

import org.choppa.model.history.History
import org.choppa.model.history.HistoryId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface HistoryRepository : JpaRepository<History, HistoryId>
