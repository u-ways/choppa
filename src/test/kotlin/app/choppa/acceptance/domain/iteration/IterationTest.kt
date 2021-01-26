package app.choppa.acceptance.domain.iteration

import app.choppa.domain.iteration.Iteration
import nl.jqno.equalsverifier.EqualsVerifier
import nl.jqno.equalsverifier.Warning
import org.junit.jupiter.api.Test

class IterationTest {
    @Test
    fun `verify the equals and hashCode contract`() {
        EqualsVerifier
            .configure().suppress(Warning.SURROGATE_KEY)
            .forClass(Iteration::class.java)
            .verify()
    }
}
