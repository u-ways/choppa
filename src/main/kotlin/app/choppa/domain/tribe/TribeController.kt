package app.choppa.domain.tribe

import app.choppa.domain.base.BaseController
import app.choppa.domain.base.BaseController.Companion.API_PREFIX
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("$API_PREFIX/tribes")
class TribeController(
    @Autowired private val tribeService: TribeService
) : BaseController<Tribe>(tribeService) {
    @GetMapping
    fun listTribes(): ResponseEntity<List<Tribe>> =
        ok().body(tribeService.find())
}
