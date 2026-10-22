plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.kotlin.compose)
  alias(libs.plugins.google.devtools.ksp)
  alias(libs.plugins.google.services) apply false
}

// Release signing reads the environment only. Nothing in the repo is a default keystore.
val keystorePath = providers.environmentVariable("KEYSTORE_PATH").orNull?.trim().orEmpty()
val storePasswordEnv = providers.environmentVariable("STORE_PASSWORD").orNull
val keyPasswordEnv = providers.environmentVariable("KEY_PASSWORD").orNull
val keystoreFile = keystorePath.takeIf { it.isNotEmpty() }?.let { file(it) }
val releaseSigningReady =
  keystoreFile != null &&
    keystoreFile.isFile &&
    !storePasswordEnv.isNullOrBlank() &&
    !keyPasswordEnv.isNullOrBlank()
val releaseSigningRequested =
  keystorePath.isNotEmpty() ||
    !storePasswordEnv.isNullOrBlank() ||
    !keyPasswordEnv.isNullOrBlank()

if (releaseSigningRequested && !releaseSigningReady) {
  val problems = buildList {
    if (keystorePath.isEmpty()) add("KEYSTORE_PATH is not set")
    else if (keystoreFile == null || !keystoreFile.isFile) add("KEYSTORE_PATH is not an existing file")
    if (storePasswordEnv.isNullOrBlank()) add("STORE_PASSWORD is not set")
    if (keyPasswordEnv.isNullOrBlank()) add("KEY_PASSWORD is not set")
  }
  error(
    "Release signing is incomplete (${problems.joinToString("; ")}). " +
      "Set KEYSTORE_PATH, STORE_PASSWORD, and KEY_PASSWORD together. The key alias is \"upload\"."
  )
}

val releaseVersionCode =
  providers.environmentVariable("GITHUB_RUN_NUMBER").orNull?.toIntOrNull() ?: 1
val releaseVersionName =
  providers.environmentVariable("VERSION_NAME").orNull?.trim()?.takeIf { it.isNotEmpty() } ?: "1.0"

android {
  // Namespace stays com.example. applicationId is the Play / Firebase package.
  namespace = "com.example"
  compileSdk { version = release(36) { minorApiLevel = 1 } }

  defaultConfig {
    applicationId = "sa.sbtech.todo"
    minSdk = 24
    targetSdk = 36
    versionCode = releaseVersionCode
    versionName = releaseVersionName

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }

  if (releaseSigningReady && keystoreFile != null) {
    signingConfigs {
      create("release") {
        storeFile = keystoreFile
        storePassword = storePasswordEnv
        keyAlias = "upload"
        keyPassword = keyPasswordEnv
      }
    }
  }

  buildTypes {
    release {
      isCrunchPngs = false
      isMinifyEnabled = true
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
      if (releaseSigningReady) {
        signingConfig = signingConfigs.getByName("release")
      }
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }
  buildFeatures {
    compose = true
    buildConfig = true
  }
  testOptions { unitTests { isIncludeAndroidResources = true } }
  dependenciesInfo {
    includeInApk = false
    includeInBundle = true
  }
}

// assembleRelease / bundleRelease both depend on this task. Debug builds do not.
tasks.configureEach {
  if (name == "preReleaseBuild" && !releaseSigningReady) {
    doFirst {
      error(
        "Refusing to build the release variant without signing secrets. " +
          "Set KEYSTORE_PATH, STORE_PASSWORD, and KEY_PASSWORD (key alias \"upload\"). " +
          "Debug builds do not need a keystore."
      )
    }
  }
}

dependencies {
  implementation(platform(libs.androidx.compose.bom))
  implementation(libs.androidx.activity.compose)
  implementation(libs.androidx.compose.material.icons.core)
  implementation(libs.androidx.compose.material.icons.extended)
  implementation(libs.androidx.compose.material3)
  implementation(libs.androidx.compose.ui)
  implementation(libs.androidx.compose.ui.graphics)
  implementation(libs.androidx.compose.ui.tooling.preview)
  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.lifecycle.runtime.compose)
  implementation(libs.androidx.lifecycle.runtime.ktx)
  implementation(libs.androidx.lifecycle.viewmodel.compose)
  implementation(libs.androidx.navigation.compose)
  implementation(libs.androidx.room.ktx)
  implementation(libs.androidx.room.runtime)
  implementation(libs.coil.compose)
  implementation(libs.converter.moshi)
  implementation(libs.kotlinx.coroutines.android)
  implementation(libs.kotlinx.coroutines.core)
  implementation(libs.logging.interceptor)
  implementation(libs.moshi.kotlin)
  implementation(libs.okhttp)
  implementation(libs.retrofit)
  implementation("androidx.biometric:biometric:1.1.0")
  implementation("com.google.android.gms:play-services-location:21.3.0")
  implementation("com.patrykandpatrick.vico:compose:1.16.0")
  implementation("com.patrykandpatrick.vico:compose-m3:1.16.0")
  implementation(platform(libs.firebase.bom))
  implementation(libs.firebase.firestore)
  implementation(libs.firebase.auth)
  implementation(libs.firebase.ai)
  implementation(libs.firebase.appcheck.playintegrity)
  debugImplementation(libs.firebase.appcheck.debug)
  implementation(libs.androidx.credentials)
  implementation(libs.androidx.credentials.play.services)
  implementation(libs.googleid)
  testImplementation(libs.androidx.compose.ui.test.junit4)
  testImplementation(libs.androidx.core)
  testImplementation(libs.androidx.junit)
  testImplementation(libs.junit)
  testImplementation(libs.kotlinx.coroutines.test)
  testImplementation(libs.robolectric)
  androidTestImplementation(platform(libs.androidx.compose.bom))
  androidTestImplementation(libs.androidx.compose.ui.test.junit4)
  androidTestImplementation(libs.androidx.espresso.core)
  androidTestImplementation(libs.androidx.junit)
  androidTestImplementation(libs.androidx.runner)
  debugImplementation(libs.androidx.compose.ui.test.manifest)
  debugImplementation(libs.androidx.compose.ui.tooling)
  "ksp"(libs.androidx.room.compiler)
  "ksp"(libs.moshi.kotlin.codegen)
}

// Optional at build time. Place the file at app/google-services.json (gitignored).
// CI writes it from the GOOGLE_SERVICES_JSON secret; do not commit it.
if (file("google-services.json").exists()) {
  apply(plugin = "com.google.gms.google-services")
}
