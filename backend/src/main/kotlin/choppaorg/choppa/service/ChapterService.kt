package choppaorg.choppa.service

import choppaorg.choppa.model.Chapter
import choppaorg.choppa.repository.ChapterRepository
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class ChapterService (
    private val chapterRepository: ChapterRepository
) {

    @Transactional
    fun create(name: String): Chapter {
        return chapterRepository.save(Chapter(name))
    }

}