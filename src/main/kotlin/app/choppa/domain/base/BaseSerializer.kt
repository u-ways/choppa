package app.choppa.domain.base

import app.choppa.domain.chapter.ChapterController
import app.choppa.domain.history.HistoryController
import app.choppa.domain.iteration.IterationController
import app.choppa.domain.member.MemberController
import app.choppa.domain.squad.SquadController
import app.choppa.utils.ReverseRouter
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.ser.std.StdSerializer
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
