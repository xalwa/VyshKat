plugins {
    kotlin("jvm") version "2.4.20"
    id("org.jetbrains.kotlinx.kover") version "0.9.8"
    application
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.insert-koin:koin-core:4.2.2")

    testImplementation(kotlin("test-junit5"))
    testImplementation(platform("org.junit:junit-bom:6.0.3"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
    jvmToolchain(21)
}

application {
    mainClass.set("MainKt")
}

tasks.test {
    useJUnitPlatform()
}

kover {
    reports {
        filters {
            excludes {
                classes("MainKt")
            }
        }
    }
}
