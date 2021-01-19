package app.choppa.domain.squad.history

import app.choppa.utils.NoArg
import java.io.Serializable
import java.time.Instant
import java.time.Instant.now
import java.util.*

@NoArg
data class SquadMemberHistoryId(
    val squad: UUID,
    val member: UUID,
    val createDate: Instant = now(),
) : Serializable
