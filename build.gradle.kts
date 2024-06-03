import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "2.0.0"
    id("com.google.devtools.ksp") version "2.0.0-1.0.21"
}

group = "creativeDSLs"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    google()
}

dependencies {
    api("org.apache.commons:commons-numbers-complex:1.1")
    api("org.junit.platform:junit-platform-commons:1.10.2")
    api("org.jetbrains.kotlin:kotlin-stdlib-jdk8:2.0.0")
    api("org.jetbrains.kotlin:kotlin-reflect:2.0.0")
    api("com.squareup:kotlinpoet:1.17.0")
    api("com.github.h0tk3y.betterParse:better-parse-jvm:0.4.4")
    api("com.faendir.kotlin.autodsl:annotations:2.2.15")
    implementation("io.kotest:kotest-runner-junit5-jvm:5.9.0")
    ksp(project(":unitsKSP"))
    ksp(project(":patternKSP"))
    implementation(project(":unitsAnnotations"))
    implementation(project(":patternAnnotations"))
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5:2.0.0")
    testImplementation("org.assertj:assertj-core:3.25.3")
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
