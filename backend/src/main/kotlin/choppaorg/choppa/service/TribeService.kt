package choppaorg.choppa.service

import choppaorg.choppa.repository.TribeRepository
import org.springframework.stereotype.Service

@Service
class TribeService(
    private val tribeRepository: TribeRepository
) {

}