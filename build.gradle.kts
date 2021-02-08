import org.gradle.api.tasks.testing.logging.TestExceptionFormat.SHORT
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

plugins {
    id("org.jlleitschuh.gradle.ktlint") version "9.4.1"
    id("org.springframework.boot") version "2.3.4.RELEASE"
    id("io.spring.dependency-management") version "1.0.10.RELEASE"
    kotlin("plugin.jpa") version "1.4.10"
    kotlin("plugin.spring") version "1.4.10"
    kotlin("jvm") version "1.4.10"
}

group = "app.choppa"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    runtimeOnly("org.postgresql:postgresql")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    developmentOnly("org.springframework.boot:spring-boot-devtools")

    implementation("org.flywaydb:flyway-core")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.okta.spring:okta-spring-boot-starter:1.2.0")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    testImplementation("org.springframework:spring-core:5.2.9.RELEASE")
    testImplementation("org.springframework:spring-test:5.2.9.RELEASE")
    testImplementation("org.springframework.boot:spring-boot-test:2.3.4.RELEASE")
    testImplementation("org.springframework.boot:spring-boot-test-autoconfigure:2.3.4.RELEASE")

    testImplementation("org.testcontainers:testcontainers:1.15.0-rc2")
    testImplementation("org.testcontainers:postgresql:1.15.0-rc2")
    testImplementation("org.testcontainers:junit-jupiter:1.15.0-rc2")

    testImplementation("com.ninja-squad:springmockk:2.0.3")
    testImplementation("io.mockk:mockk:1.10.2")
    testImplementation("com.natpryce:hamkrest:1.8.0.1")
    testImplementation("org.amshove.kluent:kluent:1.63")

    testImplementation("com.nfeld.jsonpathkt:jsonpathkt:2.0.0")
    testImplementation("org.skyscreamer:jsonassert:1.5.0")

    testImplementation("nl.jqno.equalsverifier:equalsverifier:3.5")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.2")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.6.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.6.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.6.2")
}

configurations {
    testImplementation {
        exclude(group = "hamcrest")
        exclude(module = "mockito-core")
    }
}

/** Kotlin linter settings ************************/

ktlint {
    version.set("0.38.1")
    debug.set(false)
    verbose.set(true)
    android.set(false)
    outputToConsole.set(true)
    coloredOutput.set(true)
    ignoreFailures.set(false)

    reporters {
        reporter(ReporterType.HTML)
    }

    filter {
        include("**/kotlin/**")
    }
}

/** Set noArg eligible annotations ************************/

noArg {
    annotation("app.choppa.utils.NoArg")
    invokeInitializers = true
}

/** Gradle tasks ************************/

tasks.register<Test>("acceptance") {
    description = "Runs all acceptance tests."
    maxParallelForks = 3
    include("app/choppa/acceptance/**")
    filter {
        includeTestsMatching("*Test")
    }
    failFast = true
}

tasks.register<Test>("integration") {
    description = "Runs all integration tests."
    include("app/choppa/integration/**")
    filter {
        includeTestsMatching("*IT")
    }
    failFast = true
}

tasks.withType<Test> {
    group = "test"
    reports.html.isEnabled = true
    reports.junitXml.isEnabled = false
    testLogging.exceptionFormat = SHORT
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}
