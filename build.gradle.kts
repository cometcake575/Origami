plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "com.origamimc"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":core"))
    implementation(project(":paper"))
    implementation(project(":velocity"))
}
