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
        maven {
            url = uri("https://repository.kotzilla.io/repository/kotzilla-platform/")
            url = uri("https://repository.kotzilla.io/repository/Koin-Embedded/")
        }
    }
}


include(":ferret-lib")
project(":ferret-lib").projectDir = file("ferret")