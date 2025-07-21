plugins {
    alias(libs.plugins.note.android.library)
}

android {
    namespace = "com.note.data"
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
            isReturnDefaultValues = true
        }
    }
}

dependencies {
    implementation(project(":common"))
    implementation(project(":database"))

    testImplementation(libs.androidx.test.core)
    testImplementation(libs.androidx.test.runner)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockito.core)
    testImplementation(libs.kotlin.mockito.kotlin)
}
