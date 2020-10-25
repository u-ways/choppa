package org.choppa.support.testcontainers

import org.testcontainers.containers.PostgreSQLContainer

class TestDBContainer private constructor() : PostgreSQLContainer<TestDBContainer?>(
    IMAGE_VERSION
) {
    override fun start() {
        super.start()
        System.setProperty("DB_URL", INSTANCE?.jdbcUrl)
        System.setProperty("DB_USERNAME", INSTANCE?.username)
        System.setProperty("DB_PASSWORD", INSTANCE?.password)
    }

    // allow the JVM to handle the container shutdown
    override fun stop() = Unit

    companion object {
        private const val IMAGE_VERSION = "postgres:12.4"

        @Volatile
        private var INSTANCE: TestDBContainer? = null

        fun get(): TestDBContainer =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: TestDBContainer().also { INSTANCE = it }
            }
    }
}
