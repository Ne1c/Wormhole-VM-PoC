plugins {
    kotlin("jvm")
    application
}

group = "x1.vm"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":hypervisor_api"))
    implementation(kotlin("stdlib"))
}

application {
    mainClass.set("HypervisorKt")
}