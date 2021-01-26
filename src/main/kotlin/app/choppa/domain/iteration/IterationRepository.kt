package app.choppa.domain.iteration

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface IterationRepository : JpaRepository<Iteration, UUID>
