package org.choppa.domain.iteration

import org.choppa.domain.base.BaseController
import org.choppa.domain.base.BaseController.Companion.ID_PATH
import org.choppa.domain.member.Member
import org.choppa.domain.squad.Squad
import org.choppa.domain.tribe.Tribe
import org.choppa.utils.QueryComponent
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
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("api/iterations")
class IterationController(
    @Autowired private val iterationService: IterationService,
) {

    @GetMapping
    fun listIterations(
        @QueryComponent(Member::class) @RequestParam(name = "member", required = false) memberId: UUID?,
        @QueryComponent(Squad::class) @RequestParam(name = "squad", required = false) squadId: UUID?,
        @QueryComponent(Tribe::class) @RequestParam(name = "tribe", required = false) tribeId: UUID?,
    ): ResponseEntity<List<Iteration>> =
        ok().body(iterationService.find())

    @GetMapping(ID_PATH)
    fun getIteration(@PathVariable id: UUID): ResponseEntity<Iteration> =
        ok().body(iterationService.find(id))

    @PutMapping(ID_PATH)
    fun updateIteration(@PathVariable id: UUID, @RequestBody updatedIteration: Iteration): ResponseEntity<Iteration> {
        iterationService.find(id)
        iterationService.save(updatedIteration)
        return created(BaseController.location(ID_PATH, id)).build()
    }

    @DeleteMapping(ID_PATH)
    fun deleteIteration(@PathVariable id: UUID): ResponseEntity<Iteration> {
        val iteration = iterationService.find(id)
        iterationService.delete(iteration)
        return noContent().build()
    }

    @PostMapping
    fun postIteration(@RequestBody newIteration: Iteration): ResponseEntity<Iteration> {
        val iteration = iterationService.save(newIteration)
        return created(BaseController.location(ID_PATH, iteration.id)).build()
    }
}
