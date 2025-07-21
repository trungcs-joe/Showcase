import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `kotlin-dsl`
}

group = "com.google.samples.apps.nowinandroid.buildlogic"

// Configure the build-logic plugins to target JDK 17
// This matches the JDK used to build the project, and is not related to what is running on device.
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
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.android.tools.common)
    compileOnly(libs.compose.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)
    compileOnly(libs.room.gradlePlugin)
}

tasks {
    validatePlugins {
        enableStricterValidation = true
        failOnWarning = true
    }
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "note.android.application"
            implementationClass = "AndroidApplicationPlugin"
        }

        register("androidLibrary") {
            id = "note.android.library"
            implementationClass = "AndroidLibraryPlugin"
        }
        register("androidHilt") {
            id = "note.android.hilt"
            implementationClass = "AndroidHiltPlugin"
        }
        register("androidRoom") {
            id = "note.android.room"
            implementationClass = "AndroidRoomPlugin"
        }
        register("androidLibraryCompose") {
            id = "note.android.library.compose"
            implementationClass = "AndroidLibraryComposePlugin"
        }

        register("androidApplicationCompose") {
            id = "note.android.application.compose"
            implementationClass = "AndroidApplicationComposePlugin"
        }
    }
}
