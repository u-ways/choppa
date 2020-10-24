package org.choppa.controller

import org.choppa.model.Iteration
import org.choppa.model.relations.IterationHistory
import org.choppa.service.IterationService
import org.choppa.service.relations.IterationHistoryService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/domain/iterations")
class IterationController(
    @Autowired private val iterationService: IterationService,
    @Autowired private val iterationHistoryService: IterationHistoryService
) {

    @GetMapping()
    fun getAllIterations(): List<Iteration> {
        return iterationService.find()
    }

    @GetMapping("/history")
    fun getIterationHistory(): List<IterationHistory> {
        return iterationHistoryService.find()
    }
}
