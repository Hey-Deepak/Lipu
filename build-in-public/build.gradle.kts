plugins {
    kotlin("jvm") version "1.9.22"
    kotlin("plugin.serialization") version "1.9.22"
    application
}

group = "com.streamliners.buildinpublic"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    // Kotlin Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")

    // JGit - Git operations in Kotlin
    implementation("org.eclipse.jgit:org.eclipse.jgit:6.8.0.202311291450-r")

    // Kotlin CLI argument parsing
    implementation("org.jetbrains.kotlinx:kotlinx-cli:0.3.6")

    // Testing
    testImplementation(kotlin("test"))
}

application {
    mainClass.set("com.streamliners.buildinpublic.MainKt")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}
