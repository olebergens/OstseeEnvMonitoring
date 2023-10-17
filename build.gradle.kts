import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.20"
    id("org.openjfx.javafxplugin") version "0.1.0"
    id("com.github.johnrengelman.shadow") version "7.1.0"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}


dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.6.0")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.6.0")
    implementation("org.jfree:jfreechart:1.5.4")
    implementation("org.xerial:sqlite-jdbc:3.40.1.0")
    implementation("no.tornado:tornadofx:1.7.20")
    implementation("org.openjfx:javafx-controls:19.0.2.1")
    implementation("org.openjfx:javafx-base:19.0.2.1")
    implementation("org.openjfx:javafx-graphics:19.0.2.1")
    implementation("org.openjfx:javafx-fxml:19.0.2.1")
    implementation("org.openjfx:javafx-swing:19.0.2.1")
    implementation("mysql:mysql-connector-java:8.0.23")
    implementation("com.jfoenix:jfoenix:9.0.1")

}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks {
    shadowJar {
        manifest {
            attributes["Main-Class"] = "OstseeEnvMonitoring"
        }
        sourceSets.main.get().output.resourcesDir?.let {
            from(it) {
                include("META-INF/**.")
            }
        }
    }
}