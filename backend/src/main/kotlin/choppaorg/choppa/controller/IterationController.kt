package choppaorg.choppa.controller

import choppaorg.choppa.model.Iteration
import choppaorg.choppa.model.relations.IterationHistory
import choppaorg.choppa.service.IterationService
import choppaorg.choppa.service.relations.IterationHistoryService
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