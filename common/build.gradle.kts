plugins {
    alias(libs.plugins.note.android.library)
    id("kotlinx-serialization")
}

android {
    namespace = "com.note.common"
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
            isReturnDefaultValues = true
        }
    }
}

dependencies {
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.turbine)
}
