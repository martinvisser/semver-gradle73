import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

plugins {
    `java-gradle-plugin`
    `maven-publish`
    idea
    `kotlin-dsl`
    id("net.thauvin.erik.gradle.semver") version "1.0.4"
}

group = "com.example"

repositories {
    mavenCentral()
}

gradlePlugin {
    plugins {
        create("example") {
            id = "com.example.plugin.semver"
            implementationClass = "com.example.plugin.ExamplePlugin"
            displayName = "Example"
            description = "Description"
        }
    }
}

publishing {
    repositories {
        mavenLocal()
    }
}

tasks {
    incrementBuildMeta {
        doFirst {
            buildMeta = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
        }
    }
}
