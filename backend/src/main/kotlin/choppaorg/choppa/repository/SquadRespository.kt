package choppaorg.choppa.repository

import choppaorg.choppa.model.Squad
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface SquadRepository : CrudRepository<Squad, Int> {
    override fun findById(id: Int) : Optional<Squad>
}