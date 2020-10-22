package choppaorg.choppa.repository

import choppaorg.choppa.model.Tribe
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface TribeRepository : CrudRepository<Tribe, Int>