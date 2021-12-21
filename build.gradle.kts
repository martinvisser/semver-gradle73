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
    isAutomatedPublishing = false

    plugins {
        register("example") {
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

    // Overrides the behaviour gradlePlugin would add if isAutomatedPublishing is set to true
    publications {
        register<MavenPublication>("pluginMaven") {
            from(components["java"])
        }

        gradlePlugin.plugins.all {
            register<MavenPublication>("${name}PluginMarkerMaven") {
                groupId = id
                artifactId = "${id}.gradle.plugin"

                pom.withXml {
                    asNode().appendNode("name", displayName)
                    asNode().appendNode("description", description)
                    val dependency = asNode().appendNode("dependencies").appendNode("dependency")
                    dependency.appendNode("groupId", group)
                    dependency.appendNode("artifactId", rootProject.name)
                    dependency.appendNode("version", version)
                }
            }
        }
    }
}

tasks {
    incrementBuildMeta {
        doFirst {
            buildMeta = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
        }
    }
}
