plugins {
    id("java")
}

group = "com.origamimc"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    compileOnly("org.jetbrains:annotations:23.0.0")
    compileOnly("org.slf4j:slf4j-jdk14:2.0.9")
}

tasks.test {
    useJUnitPlatform()
}