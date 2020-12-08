package app.choppa.acceptance.domain.tribe

import app.choppa.domain.history.History
import app.choppa.domain.squad.Squad
import app.choppa.domain.tribe.Tribe
import app.choppa.support.factory.HistoryFactory
import nl.jqno.equalsverifier.EqualsVerifier
import nl.jqno.equalsverifier.Warning
import org.junit.jupiter.api.Test

class TribeTest {
    @Test
    fun `verify the equals and hashCode contract`() {
        EqualsVerifier
            .configure().suppress(Warning.SURROGATE_KEY)
            .forClass(Tribe::class.java)
            .withPrefabValues(Squad::class.java, Squad(), Squad())
            .withPrefabValues(History::class.java, HistoryFactory.create(), HistoryFactory.create())
            .verify()
    }
}
