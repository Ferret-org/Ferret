pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://dl.google.com/dl/android/maven2/")
    }
}

rootProject.name = "Ferret"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

// Ferret library
include(":ferret-lib")
project(":ferret-lib").projectDir = file("ferret")

// Sample app modules (physically inside app/)
include(":androidApp")
project(":androidApp").projectDir = file("app/androidApp")

include(":shared")
project(":shared").projectDir = file("app/shared")
