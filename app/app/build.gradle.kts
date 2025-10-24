import com.codingfeline.buildkonfig.compiler.FieldSpec.Type
import com.michaelflisar.kmplibrary.BuildFilePlugin
import com.michaelflisar.kmplibrary.DesktopSetup
import com.michaelflisar.kmplibrary.Targets
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import com.michaelflisar.kmplibrary.setupWindowsApp
import kotlin.jvm.java


plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.compose)
    alias(deps.plugins.kmplibrary.buildplugin)
    alias(deps.plugins.kmplibrary.projectplugin)
    alias(libs.plugins.buildkonfig)
}

// get build file plugin
val buildFilePlugin = project.plugins.getPlugin(BuildFilePlugin::class.java)

// -------------------
// Informations
// -------------------

val appVersionName = "0.0.1"
val appVersionCode = 1

val appName = "Tests"
val androidNamespace = "com.michaelflisar.tests.app"

val buildTargets = Targets(
    // mobile
    android = true,
    //iOS = true,
    // desktop
    windows = true,
    //macOS = true,
    // web
    wasm = false // vico does not support it
)

val desktopSetup = DesktopSetup(
    appName = appName,
    appVersionName = appVersionName,
    mainClass = "$androidNamespace.MainKt",
    author = "Michael Flisar",
    ico = "logo.ico"
)

// -------------------
// Setup
// -------------------

dependencies {
    coreLibraryDesugaring(libs.desugar)
}

buildkonfig {
    packageName = androidNamespace
    defaultConfigs {
        buildConfigField(Type.STRING, "versionName", appVersionName)
        buildConfigField(Type.INT, "versionCode", appVersionCode.toString())
        buildConfigField(Type.STRING, "packageName", androidNamespace)
    }
}

kotlin {

    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }

    //-------------
    // Targets
    //-------------

    buildFilePlugin.setupTargetsApp(
        buildTargets,
        wasmModuleName = "tests",
        wasmOutputFileName = "tests.js"
    )

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

            // modules
            implementation(project(":app:modules:core"))
            implementation(project(":app:modules:test-vico"))

            // voyager
            implementation(deps.voyager.navigator)
            implementation(deps.voyager.transitions)

        }
    }
}

// -------------------
// Configurations
// -------------------

// android configuration
android {

    compileOptions {
        isCoreLibraryDesugaringEnabled = true
    }

    buildFilePlugin.setupAndroidApp(
        androidNamespace = androidNamespace,
        compileSdk = app.versions.compileSdk,
        minSdk = app.versions.minSdk,
        targetSdk = app.versions.targetSdk,
        versionCode = appVersionCode,
        versionName = appVersionName,
        buildConfig = true,
        checkDebugKeyStoreProperty = true
    )
}

// windows configuration
compose.desktop {
    application {

        // BuildFilePlugin Extension
        setupWindowsApp(
            project = project,
            setup = desktopSetup,
            configNativeDistribution = {

                // targets
                targetFormats(TargetFormat.Exe)

            }
        )
    }
}