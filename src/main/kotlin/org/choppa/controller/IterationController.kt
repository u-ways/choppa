package org.choppa.controller

import org.choppa.model.history.History
import org.choppa.model.iteration.Iteration
import org.choppa.service.HistoryService
import org.choppa.service.IterationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/domain/iterations")
class IterationController(
    @Autowired private val iterationService: IterationService,
    @Autowired private val historyService: HistoryService
) {

    @GetMapping()
    fun getAllIterations(): List<Iteration> {
        return iterationService.find()
    }

    @GetMapping("/history")
    fun getIterationHistory(): List<History> {
        return historyService.find()
    }
}
