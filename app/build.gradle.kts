plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
//    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.ksp)
}

android {
    namespace = "dev.medzik.android.preferences"
    compileSdk = 34

    defaultConfig {
        applicationId = "dev.medzik.android.preferences"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.datastore.core)
    implementation(libs.androidx.datastore)
    implementation(libs.kotlinx.serialization.json)

    implementation(projects.annotations)
    ksp(projects.annotationsProcessor)
}
