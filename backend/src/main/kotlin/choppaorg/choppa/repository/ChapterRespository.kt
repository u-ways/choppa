package choppaorg.choppa.repository

import choppaorg.choppa.model.Chapter
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ChapterRepository : CrudRepository<Chapter, Int>