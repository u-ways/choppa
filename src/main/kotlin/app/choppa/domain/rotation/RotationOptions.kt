package app.choppa.domain.rotation

import app.choppa.domain.chapter.Chapter
import app.choppa.domain.rotation.filter.Filter
import app.choppa.domain.rotation.filter.Filter.OLDEST
import app.choppa.domain.rotation.strategy.Strategy
import app.choppa.domain.rotation.strategy.Strategy.CLOCKWISE
import app.choppa.domain.chapter.Chapter.Companion.UNASSIGNED_ROLE as UNASSIGNED_ROLE1

data class RotationOptions(
    val amount: Int = 1,
    val chapter: Chapter = UNASSIGNED_ROLE1,
    val filter: Filter = OLDEST,
    val strategy: Strategy = CLOCKWISE
) {
    companion object {
        val DEFAULT_OPTIONS = RotationOptions()
    }
}
