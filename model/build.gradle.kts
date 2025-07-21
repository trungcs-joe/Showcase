plugins {
    alias(libs.plugins.note.android.library)
}

android {
    namespace = "com.note.model"
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
            isReturnDefaultValues = true
        }
    }
}

dependencies {
    api(libs.kotlinx.datetime)
}
