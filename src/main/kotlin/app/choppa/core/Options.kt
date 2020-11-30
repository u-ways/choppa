package app.choppa.core

import app.choppa.core.filter.Filter
import app.choppa.core.filter.Filter.OLDEST
import app.choppa.core.strategy.Strategy
import app.choppa.core.strategy.Strategy.CLOCKWISE
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
