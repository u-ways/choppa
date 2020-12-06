package app.choppa.core.rotation

import app.choppa.core.rotation.filter.Filter
import app.choppa.core.rotation.filter.Filter.OLDEST
import app.choppa.core.rotation.strategy.Strategy
import app.choppa.core.rotation.strategy.Strategy.CLOCKWISE
import app.choppa.domain.chapter.Chapter

data class Options(
    val amount: Int = 1,
    val chapter: Chapter = Chapter.UNASSIGNED_ROLE,
    val filter: Filter = OLDEST,
    val strategy: Strategy = CLOCKWISE
) {
    companion object {
        val DEFAULT_OPTIONS = Options()
    }
}
