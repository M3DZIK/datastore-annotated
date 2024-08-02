import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("java-library")
    alias(libs.plugins.kotlin.jvm)
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

dependencies {
    implementation(libs.symbol.processing.api)
    implementation(libs.kotlinpoet)
    implementation(libs.kotlinpoet.ksp)
    implementation(libs.kotlinx.serialization.json)

    implementation(projects.annotations)
}

publishConfig {
    artifactId = "datastore-annotations-processor"

    pom {
        name = "Annotation-based DataStore - KSP Processor"
        description = "KSP Processor for Annotation-based DataStore for Android"
    }
}
