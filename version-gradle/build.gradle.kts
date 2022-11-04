plugins {
    `kotlin-dsl`
    id("groovy") // Groovy Language
    id("java-gradle-plugin") // Java Gradle Plugin
}

repositories {
    mavenCentral()
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}


gradlePlugin {
    plugins {
        create("version.gradle") {
            id = "version.gradle"
            implementationClass = "version.gradle.VersionGradle"
        }
    }
}
