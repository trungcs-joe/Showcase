plugins {
    alias(libs.plugins.note.android.library)
    alias(libs.plugins.note.android.room)
}

android {
    namespace = "com.note.database"
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
            isReturnDefaultValues = true
        }
    }
}

dependencies {
    api(project(":model"))
    implementation(libs.kotlinx.datetime)

    androidTestImplementation(libs.androidx.test.core)
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.kotlinx.coroutines.test)

    testImplementation("org.robolectric:robolectric:4.10.3")
}
