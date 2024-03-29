package app.choppa.integration.domain.tribe

import app.choppa.domain.tribe.Tribe
import app.choppa.domain.tribe.TribeService
import app.choppa.exception.EntityNotFoundException
import app.choppa.support.base.BaseServiceIT
import app.choppa.support.factory.TribeFactory
import app.choppa.support.matchers.containsInAnyOrder
import app.choppa.utils.Color.Companion.toRGBAInt
import com.natpryce.hamkrest.assertion.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import javax.transaction.Transactional

internal class TribeServiceCollectionIT @Autowired constructor(
    private val tribeService: TribeService,
) : BaseServiceIT() {

    @Test
    @Transactional
    fun `Given a new list of tribes, when service saves said list of tribes, then service should persist the list of tribes`() {
        val newListOfTribes = TribeFactory.create(amount = 3)
        val result = tribeService.save(newListOfTribes)

        assertThat(result, List<Tribe>::containsInAnyOrder, newListOfTribes)
    }

    @Test
    @Transactional
    fun `Given an existing list of tribes, when service updates said list of tribes, then service should persist the changed list of tribes`() {
        val existingListOfTribes = tribeService.save(TribeFactory.create(amount = 3))
        val redColor = "#FF0000".toRGBAInt()

        val updatedListOfTribes = existingListOfTribes.map {
            TribeFactory.create(it.id, it.name, redColor)
        }

        val result = tribeService.save(updatedListOfTribes)

        assertThat(result, List<Tribe>::containsInAnyOrder, updatedListOfTribes)
    }

    @Test
    @Transactional
    fun `Given an existing list of tribes, when service deletes said list of tribes, then service should remove the existing list of tribes`() {
        val existingListOfTribes = tribeService.save(TribeFactory.create(amount = 3))
        val removedListOfTribes = tribeService.delete(existingListOfTribes)

        assertThrows(EntityNotFoundException::class.java) { tribeService.find(removedListOfTribes.map { it.id }) }
    }
}
