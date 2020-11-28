package org.choppa.acceptance.domain.iteration

import nl.jqno.equalsverifier.EqualsVerifier
import nl.jqno.equalsverifier.Warning
import org.choppa.domain.iteration.Iteration
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
