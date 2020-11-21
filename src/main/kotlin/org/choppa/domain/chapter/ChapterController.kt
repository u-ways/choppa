package org.choppa.domain.chapter

import org.choppa.domain.base.BaseController.Companion.ID_PATH
import org.choppa.domain.base.BaseController.Companion.location
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.created
import org.springframework.http.ResponseEntity.noContent
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("api/chapters")
class ChapterController(@Autowired private val chapterService: ChapterService) {

    @GetMapping
    fun listChapters(): ResponseEntity<List<Chapter>> =
        ok().body(chapterService.find())

    @GetMapping(ID_PATH)
    fun getChapter(@PathVariable id: UUID): ResponseEntity<Chapter> =
        ok().body(chapterService.find(id))

    @PutMapping(ID_PATH)
    fun updateChapter(@PathVariable id: UUID, @RequestBody updatedChapter: Chapter): ResponseEntity<Chapter> {
        chapterService.find(id)
        chapterService.save(updatedChapter)
        return created(location(ID_PATH, id)).build()
    }

    @DeleteMapping(ID_PATH)
    fun deleteChapter(@PathVariable id: UUID): ResponseEntity<Chapter> {
        val chapter = chapterService.find(id)
        chapterService.delete(chapter)
        return noContent().build()
    }

    @PostMapping
    fun postChapter(@RequestBody newChapter: Chapter): ResponseEntity<Chapter> {
        val chapter = chapterService.save(newChapter)
        return created(location(ID_PATH, chapter.id)).build()
    }
}
