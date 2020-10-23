package org.choppa.controller

import org.choppa.model.Chapter
import org.choppa.service.ChapterService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/domain/chapters")
class ChapterController(
    @Autowired private val chapterService: ChapterService
) {

    @GetMapping()
    fun getAllChapters(): List<Chapter> {
        return chapterService.find()
    }
}
