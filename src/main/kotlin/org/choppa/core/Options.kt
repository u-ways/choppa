package org.choppa.core

import org.choppa.core.filter.Filter
import org.choppa.core.filter.Filter.OLDEST
import org.choppa.core.strategy.Strategy
import org.choppa.core.strategy.Strategy.CLOCKWISE
import org.choppa.domain.chapter.Chapter

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
