package org.choppa.domain.base

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import org.choppa.domain.chapter.ChapterController
import org.choppa.domain.history.HistoryController
import org.choppa.domain.iteration.IterationController
import org.choppa.domain.member.MemberController
import org.choppa.domain.squad.SquadController
import org.choppa.utils.ReverseRouter
import org.springframework.beans.factory.annotation.Autowired
import kotlin.reflect.KClass
import kotlin.reflect.KFunction

abstract class BaseSerializer<T>(
    supportedClass: Class<T>?,
    @Autowired internal val reverseRouter: ReverseRouter = ReverseRouter()
) : StdSerializer<T>(supportedClass) {

    internal enum class QueryType(
        val controller: KClass<*>,
        val function: KFunction<*>
    ) {
        CHAPTERS(ChapterController::class, ChapterController::listChapters),
        MEMBERS(MemberController::class, MemberController::listMembers),
        SQUADS(SquadController::class, SquadController::listSquads),
        ITERATIONS(IterationController::class, IterationController::listIterations),
        HISTORY(HistoryController::class, HistoryController::listHistory),
    }

    internal fun <T : BaseModel> JsonGenerator.queryComponent(queryType: QueryType, type: T) {
        this.writeStringField(
            reverseRouter.route(queryType.controller),
            reverseRouter.queryComponent(queryType.controller, queryType.function, type)
        )
    }
}
