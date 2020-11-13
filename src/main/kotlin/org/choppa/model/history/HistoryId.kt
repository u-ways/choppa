package org.choppa.model.history

import org.choppa.utils.NoArg
import java.io.Serializable
import java.util.UUID

@NoArg
data class HistoryId(
    val iteration: UUID,
    val tribe: UUID,
    val squad: UUID,
    val member: UUID
) : Serializable
