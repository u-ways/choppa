package app.choppa.support.testcontainers

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers
internal class TestDBContainerTest {
    @Container
    val testDBContainer: TestDBContainer = TestDBContainer.get()

    @Test
    internal fun canRunContainer() {
        assertTrue(testDBContainer.isRunning)
    }
}
