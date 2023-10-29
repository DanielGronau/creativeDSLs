import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.0"
}

group = "creativeDSLs"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    google()
}

dependencies {
    api("org.apache.commons:commons-numbers-complex:1.0")
    api("org.junit.platform:junit-platform-commons:1.9.0")
    api("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.7.10")
    api("org.jetbrains.kotlin:kotlin-reflect:1.7.10")
    api("com.squareup:kotlinpoet:1.12.0")
    api("com.github.h0tk3y.betterParse:better-parse-jvm:0.4.4")
    api("com.faendir.kotlin.autodsl:annotations:2.2.15")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5:1.7.10")
    testImplementation("org.assertj:assertj-core:3.23.1")
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        freeCompilerArgs = freeCompilerArgs + "-Xcontext-receivers"
    }
}

tasks.test {
    useJUnitPlatform()
}

configure<SourceSetContainer> {
    named("main") {
        java.srcDir("src/main/kotlin")
    }
}

kotlin {
    jvmToolchain(11)
}
