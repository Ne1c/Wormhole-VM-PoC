plugins {
    kotlin("jvm")
    kotlin("plugin.serialization") version "1.5.31"
    application
}

group = "x1.vm"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":hypervisor_api"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.0")
}

application {
    mainClass.set("VMMainKt")
}