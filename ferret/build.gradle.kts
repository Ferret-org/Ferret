plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.vanniktech.mavenPublish)
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
    alias(libs.plugins.kotlinSerialization)
}

group = "com.ferret"
version = "1.0.0"

kotlin {
    android {
        namespace = "com.ferret.ferret"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "ferret"
            isStatic = false
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)

            implementation(libs.room.runtime)
            implementation(libs.sqlite.bundled)
            implementation(libs.kotlinx.serialization.core)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.ktor.client.core)
            implementation(libs.kotlinx.datetime)
            implementation(libs.androidx.navigation.compose)

            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)

            implementation(compose.components.uiToolingPreview)
            implementation(compose.materialIconsExtended)
        }

        androidMain.dependencies {
            implementation(libs.androidx.core.ktx)
            implementation(libs.androidx.appcompat)
            implementation(libs.core)
            implementation(libs.androidx.activity.compose)
            implementation(libs.ktor.client.okhttp)
            implementation(libs.ktor.client.websockets)

            implementation(compose.uiTooling)

            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.androidx.activity.compose)
        }

        nativeMain.dependencies {
            implementation(libs.ktor.client.websockets)
        }
    }
}

dependencies {
    add("kspAndroid", libs.room.compiler)
    add("kspIosX64", libs.room.compiler)
    add("kspIosArm64", libs.room.compiler)
    add("kspIosSimulatorArm64", libs.room.compiler)
}

room {
    schemaDirectory(layout.projectDirectory.dir("schemas"))
}

mavenPublishing {
    coordinates(
        groupId = "com.ferret",
        artifactId = "ferret",
        version = version.toString()
    )

    publishToMavenCentral()

    signAllPublications()

    pom {
        name.set("Ferret")
        description.set("KMP network inspection library")
        inceptionYear.set("2026")
        url.set("https://github.com/Ferret-org/ferret")

        licenses {
            license {
                name.set("Apache-2.0")
                url.set("https://www.apache.org/licenses/LICENSE-2.0")
            }
        }

        developers {
            developer {
                id = "Aditya-gupta99"
                name = "Aditya Gupta"
                url = "https://github.com/Aditya-gupta99"
            }
            developer {
                id.set("Nagarjuna0033")
                name.set("Nagarjuna Banda")
                url.set("https://github.com/Nagarajuna0033")
            }
        }

        scm {
            url.set("https://github.com/Ferret-org/ferret")
            connection.set("scm:git:https://github.com/Ferret-org/ferret.git")
            developerConnection.set("scm:git:ssh://git@github.com:Ferret-org/ferret.git")
        }
    }

}