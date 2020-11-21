package org.choppa.domain.chapter

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface ChapterRepository : JpaRepository<Chapter, UUID>
