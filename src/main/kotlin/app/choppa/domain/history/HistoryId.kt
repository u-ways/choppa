package app.choppa.domain.history

import app.choppa.utils.NoArg
import java.io.Serializable
import java.time.Instant
import java.util.UUID

@NoArg
data class HistoryId(
    val iteration: UUID,
    val tribe: UUID,
    val squad: UUID,
    val member: UUID,
    val createDate: Instant = Instant.now()
) : Serializable
