package choppaorg.choppa.repository

import choppaorg.choppa.model.Chapter
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ChapterRepository : JpaRepository<Chapter, UUID>