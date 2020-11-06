package org.choppa.controller

import org.choppa.model.tribe.Tribe
import org.choppa.service.TribeService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/domain/tribes")
class TribeController(
    @Autowired private val tribeService: TribeService
) {

    @GetMapping()
    fun getAllTribes(): List<Tribe> {
        return tribeService.find()
    }
}
