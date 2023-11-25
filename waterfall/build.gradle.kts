plugins {
    id("java")
}

group = "com.origamimc"
version = "1.0.0"

repositories {
    mavenCentral()
    maven { url = uri("https://repo.papermc.io/repository/maven-public/") }
}

dependencies {
    implementation("org.jetbrains:annotations:24.0.0")
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    compileOnly("io.github.waterfallmc:waterfall-api:1.20-R0.1-SNAPSHOT")
    compileOnly(project(":core"))
}

tasks.test {
    useJUnitPlatform()
}