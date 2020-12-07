package app.choppa.acceptance.domain.rotation

import app.choppa.core.rotation.Rotation
import app.choppa.core.rotation.RotationOptions
import app.choppa.core.rotation.filter.Filter
import app.choppa.core.rotation.strategy.Strategy
import app.choppa.domain.chapter.Chapter
import app.choppa.domain.chapter.ChapterService
import app.choppa.domain.history.HistoryRepository
import app.choppa.domain.history.HistoryService
import app.choppa.domain.member.Member
import app.choppa.domain.member.MemberRepository
import app.choppa.domain.member.MemberService
import app.choppa.domain.rotation.RotationService
import app.choppa.domain.squad.SquadRepository
import app.choppa.domain.squad.SquadService
import app.choppa.domain.tribe.TribeRepository
import app.choppa.domain.tribe.TribeService
import app.choppa.exception.EntityNotFoundException
import app.choppa.support.factory.TribeFactory
import io.mockk.every
import io.mockk.mockkClass
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.Optional.empty
import java.util.Optional.of
import java.util.UUID.randomUUID

internal class RotationServiceTest {
    private lateinit var memberRepository: MemberRepository
    private lateinit var chapterService: ChapterService
    private lateinit var memberService: MemberService
    private lateinit var squadService: SquadService
    private lateinit var squadRepository: SquadRepository
    private lateinit var historyService: HistoryService
    private lateinit var historyRepository: HistoryRepository
    private lateinit var tribeRepository: TribeRepository
    private lateinit var tribeService: TribeService
    private lateinit var service: RotationService

    @BeforeEach
    internal fun setUp() {
        chapterService = mockkClass(ChapterService::class, relaxed = true)
        memberRepository = mockkClass(MemberRepository::class)
        memberService = MemberService(memberRepository, chapterService)
        squadRepository = mockkClass(SquadRepository::class)
        squadService = SquadService(squadRepository, tribeService, memberService)
        tribeRepository = mockkClass(TribeRepository::class)
        tribeService = TribeService(tribeRepository)
        historyRepository = mockkClass(HistoryRepository::class)
        historyService = HistoryService(historyRepository)
        service = RotationService(tribeService, squadService, historyService)
    }
}
