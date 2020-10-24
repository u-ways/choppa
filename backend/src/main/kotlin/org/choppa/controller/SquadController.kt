package org.choppa.controller

import org.choppa.model.Squad
import org.choppa.model.relations.SquadCurrentMembers
import org.choppa.service.SquadService
import org.choppa.service.relations.SquadCurrentMembersService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/domain/squads")
class SquadController(
    @Autowired private val squadService: SquadService,
    @Autowired private val squadCurrentMembersService: SquadCurrentMembersService
) {

    @GetMapping()
    fun getAllSquads(): List<Squad> {
        return squadService.find()
    }

    @GetMapping("/members")
    fun getAllSquadCurrentMembers(): List<SquadCurrentMembers> {
        return squadCurrentMembersService.find()
    }
}
