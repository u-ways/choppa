package org.choppa.controller

import org.choppa.model.squad.Squad
import org.choppa.service.SquadService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/domain/squads")
class SquadController(
    @Autowired private val squadService: SquadService
) {

    @GetMapping()
    fun getAllSquads(): List<Squad> {
        return squadService.find()
    }
}
