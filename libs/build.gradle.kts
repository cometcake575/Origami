plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "7.1.0"
    id("io.papermc.paperweight.userdev") version "1.5.4"
}

group = "java.com.starshootercity"
version = "1.0.0"

repositories {
    mavenCentral()
    maven { url = uri("https://repo.papermc.io/repository/maven-public/") }
}

dependencies {
    implementation("org.jetbrains:annotations:23.0.0")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
    compileOnly("io.papermc.paper:paper-api:1.20.2-R0.1-SNAPSHOT")
    implementation(files("libs/velocity-3.2.0-SNAPSHOT-294.jar"))
    paperweight.paperDevBundle("1.20.2-R0.1-SNAPSHOT")
}

tasks.test {
    useJUnitPlatform()
}