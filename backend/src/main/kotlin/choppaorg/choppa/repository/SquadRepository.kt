package choppaorg.choppa.repository

import choppaorg.choppa.model.Squad
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface SquadRepository : JpaRepository<Squad, UUID>