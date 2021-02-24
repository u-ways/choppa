package app.choppa.domain.base

import app.choppa.domain.account.Account
import java.util.*

interface BaseModel {
    val id: UUID
    val account: Account
}
