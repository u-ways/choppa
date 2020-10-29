package choppaorg.choppa.repository

import choppaorg.choppa.model.Tribe
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface TribeRepository : JpaRepository<Tribe, UUID>