package app.choppa.acceptance.domain.tribe

import app.choppa.domain.squad.Squad
import app.choppa.domain.tribe.Tribe
import app.choppa.support.factory.SquadFactory
import nl.jqno.equalsverifier.EqualsVerifier
import nl.jqno.equalsverifier.Warning
import org.junit.jupiter.api.Test

class TribeTest {
    @Test
    fun `verify the equals and hashCode contract`() {
        EqualsVerifier
            .configure().suppress(Warning.SURROGATE_KEY)
            .forClass(Tribe::class.java)
            .withPrefabValues(Squad::class.java, SquadFactory.create(), SquadFactory.create())
            .verify()
    }
}
