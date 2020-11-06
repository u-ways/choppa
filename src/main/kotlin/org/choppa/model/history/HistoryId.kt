package org.choppa.model.history

import java.io.Serializable
import java.util.UUID

class HistoryId(
    val iteration: UUID = UUID.randomUUID(),
    val tribe: UUID = UUID.randomUUID(),
    val squad: UUID = UUID.randomUUID(),
    val member: UUID = UUID.randomUUID()
) : Serializable
