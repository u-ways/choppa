package app.choppa.integration.domain.squad

import app.choppa.domain.member.Member.Companion.NO_MEMBERS
import app.choppa.domain.squad.Squad
import app.choppa.domain.squad.SquadController
import app.choppa.domain.squad.SquadService
import app.choppa.exception.EmptyListException
import app.choppa.exception.EntityNotFoundException
import app.choppa.support.base.BaseControllerIT
import app.choppa.support.factory.MemberFactory
import app.choppa.support.factory.SquadFactory
import app.choppa.support.factory.TribeFactory
import app.choppa.utils.Color.Companion.GREY
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.hamcrest.core.StringContains
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.HttpHeaders.LOCATION
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put
import java.util.*

@WebMvcTest(controllers = [SquadController::class])
internal class SquadControllerIT : BaseControllerIT() {
    @MockkBean
    private lateinit var squadService: SquadService

    private lateinit var squad: Squad

    @BeforeEach
    internal fun setUp() {
        squad = SquadFactory.create()
    }

    @Nested
    @WithMockUser
    inner class HappyPath {

        @Test
        fun `LIST entities`() {
            val anotherSquad = SquadFactory.create()
            val entities = listOf(squad, anotherSquad)

            every { squadService.find() } returns entities

            mvc.get("/api/squads") {
                contentType = APPLICATION_JSON
                accept = APPLICATION_JSON
            }.andExpect {
                status { isOk }
                content { contentType(APPLICATION_JSON) }
                content { json(mapper.writeValueAsString(entities)) }
            }
        }

        @Test
        fun `GET entity by ID`() {
            val entity = squad.apply {
                this.members.add(MemberFactory.create())
                this.members.add(MemberFactory.create())
            }

            every { squadService.find(entity.id) } returns entity

            mvc.get("/api/squads/{id}", entity.id) {
                contentType = APPLICATION_JSON
                accept = APPLICATION_JSON
            }.andExpect {
                status { isOk }
                content { contentType(APPLICATION_JSON) }
                content { json(mapper.writeValueAsString(entity)) }
            }
        }

        @Test
        fun `PUT entity by ID`() {
            val entity = squad
            val updatedEntity = SquadFactory.create(squad.id, squad.name, GREY, TRIBE, NO_MEMBERS)

            every { squadService.find(entity.id) } returns entity
            every {
                squadService.save(
                    SquadFactory.create(
                        squad.id,
                        squad.name,
                        squad.color,
                        TribeFactory.create(squad.tribe.id),
                        squad.members
                    )
                )
            } returns updatedEntity

            mvc.put("/api/squads/{id}", entity.id) {
                contentType = APPLICATION_JSON
                accept = APPLICATION_JSON
                content = mapper.writeValueAsString(updatedEntity)
            }.andExpect {
                status { isCreated }
                header { string(LOCATION, StringContains("/api/squads/${entity.id}")) }
            }
        }

        @Test
        fun `DELETE entity by ID`() {
            val entity = squad

            every { squadService.find(entity.id) } returns entity
            every { squadService.delete(entity) } returns entity

            mvc.delete("/api/squads/{id}", entity.id) {
                contentType = APPLICATION_JSON
                accept = APPLICATION_JSON
            }.andExpect {
                status { isNoContent }
            }
        }

        @Test
        fun `POST new entity`() {
            val newEntity = squad

            every {
                squadService.save(
                    SquadFactory.create(
                        squad.id,
                        squad.name,
                        squad.color,
                        TribeFactory.create(squad.tribe.id)
                    )
                )
            } returns newEntity

            mvc.post("/api/squads/${newEntity.id}") {
                contentType = APPLICATION_JSON
                accept = APPLICATION_JSON
                content = mapper.writeValueAsString(newEntity)
            }.andExpect {
                status { isCreated }
                header { string(LOCATION, StringContains("/api/squads/${newEntity.id}")) }
            }
        }
    }

    @Nested
    @WithMockUser
    inner class SadPath {

        @Test
        fun `LIST no content`() {
            every { squadService.find() } throws EmptyListException("No squads exist yet")

            mvc.get("/api/squads") {
                contentType = APPLICATION_JSON
                accept = APPLICATION_JSON
            }.andExpect {
                status { isNoContent }
            }
        }

        @Test
        fun `GET UUID doesn't exist`() {
            val randomUUID = UUID.randomUUID()

            every { squadService.find(randomUUID) } throws EntityNotFoundException("Squad with id [$randomUUID] does not exist.")

            mvc.get("/api/squads/{id}", randomUUID) {
                contentType = APPLICATION_JSON
                accept = APPLICATION_JSON
            }.andExpect {
                status { isNotFound }
            }
        }

        @Test
        fun `PUT invalid payload`() {
            val randomUUID = UUID.randomUUID()

            mvc.put("/api/squads/{id}", randomUUID) {
                contentType = APPLICATION_JSON
                accept = APPLICATION_JSON
                content = "invalidPayload"
            }.andExpect {
                status { isUnprocessableEntity }
            }
        }

        @Test
        fun `DELETE UUID doesn't exist`() {
            val randomUUID = UUID.randomUUID()

            every { squadService.find(randomUUID) } throws EntityNotFoundException("Squad with id [$randomUUID] does not exist.")

            mvc.delete("/api/squads/{id}", randomUUID) {
                contentType = APPLICATION_JSON
                accept = APPLICATION_JSON
            }.andExpect {
                status { isNotFound }
            }
        }

        @Test
        fun `POST invalid payload`() {
            mvc.post("/api/squads") {
                contentType = APPLICATION_JSON
                accept = APPLICATION_JSON
                content = "invalidPayload"
            }.andExpect {
                status { isUnprocessableEntity }
            }
        }

        @Test
        fun `POST invalid color payload`() {
            mvc.post("/api/squads") {
                contentType = APPLICATION_JSON
                accept = APPLICATION_JSON
                content =
                    """
                        {
                            "id": "squads/${squad.id}",
                            "name": "${squad.name}",
                            "color": "#xfxfxf"
                        }
                    """.trimIndent()
            }.andExpect {
                status { isUnprocessableEntity }
            }
        }
    }
}
