plugins {
    id("java")
}

group = "com.origamimc"
version = "1.0.0"

repositories {
    mavenCentral()
    maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots") }
}

dependencies {
    implementation("org.jetbrains:annotations:24.0.0")
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    compileOnly("org.slf4j:slf4j-jdk14:2.0.9")
    compileOnly("net.md-5:bungeecord-api:1.20-R0.1")
    compileOnly(project(":core"))
}

tasks.test {
    useJUnitPlatform()
}