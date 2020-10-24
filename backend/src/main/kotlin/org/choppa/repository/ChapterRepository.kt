package org.choppa.repository

import org.choppa.model.Chapter
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface ChapterRepository : JpaRepository<Chapter, UUID>
