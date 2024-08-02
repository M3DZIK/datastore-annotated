plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.dokka) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.ksp) apply false
//    alias(libs.plugins.nmcp)
}

//nmcp {
//    publishAggregation() {
//        project(":annotations")
//        project(":annotations-processor")
//
//        username = project.properties["sonatype.username"] as String
//        password = project.properties["sonatype.password"] as String
//        publicationType = "USER_MANAGED"
//    }
//}
