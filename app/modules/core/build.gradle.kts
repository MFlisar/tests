import com.michaelflisar.kmplibrary.BuildFilePlugin
import com.michaelflisar.kmplibrary.Target
import com.michaelflisar.kmplibrary.Targets

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.compose)
    alias(deps.plugins.kmplibrary.buildplugin)
}

// get build file plugin
val buildFilePlugin = project.plugins.getPlugin(BuildFilePlugin::class.java)

// -------------------
// Informations
// -------------------

val androidNamespace = "com.michaelflisar.tests.core"

val buildTargets = Targets(
    // mobile
    android = true,
    iOS = false,
    // desktop
    windows = true,
    macOS = false,
    // web
    wasm = true
)

// -------------------
// Setup
// -------------------

compose.resources {
    packageOfResClass = "$androidNamespace.resources"
    publicResClass = true
}

kotlin {

    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }

    //-------------
    // Targets
    //-------------

    buildFilePlugin.setupTargetsLibrary(buildTargets)

    // -------
    // Sources
    // -------

    sourceSets {

        // ---------------------
        // custom shared sources
        // ---------------------

        // --

        // ---------------------
        // dependencies
        // ---------------------

        commonMain.dependencies {

            // resources
            api(compose.components.resources)

            // compose
            api(compose.runtime)
            api(libs.compose.ui)
            api(libs.compose.ui.backhandler)
            api(libs.compose.material3)
            api(libs.compose.material.icons.core)
            api(libs.compose.material.icons.extended)

            // mflisar
            api(deps.kmp.parcelize)
        }

        jvmMain.dependencies {

            api(compose.desktop.currentOs) {
                exclude(group = "org.jetbrains.compose.material", module = "material")
            }

        }

        androidMain.dependencies {

            api(androidx.activity.compose)
            api(deps.material)

        }
    }
}

// -------------------
// Configurations
// -------------------

// android configuration
android {

    buildFilePlugin.setupAndroidLibrary(
        androidNamespace = androidNamespace,
        compileSdk = app.versions.compileSdk,
        minSdk = app.versions.minSdk,
        buildConfig = false
    )
}