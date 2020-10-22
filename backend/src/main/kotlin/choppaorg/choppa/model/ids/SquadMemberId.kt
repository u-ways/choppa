package choppaorg.choppa.model.ids

import java.io.Serializable

class SquadMemberId(

    var squad: Int? = null,

    var member: Int? = null

) : Serializable {
    constructor() : this(null, null)
}