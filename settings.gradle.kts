rootProject.name = "Ferret"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

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


include(":ferret-lib")
project(":ferret-lib").projectDir = file("ferret")