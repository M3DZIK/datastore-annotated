import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("java-library")
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.nmcp)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_17
    }
}

publishConfig {
    artifactId = "datastore-annotations"

    pom {
        name = "Annotation-based DataStore"
        description = "Annotation-based DataStore for Android"
    }
}

nmcp {
    publishAllPublications {
        username = project.properties["sonatype.username"] as String
        password = project.properties["sonatype.password"] as String
    }
}
