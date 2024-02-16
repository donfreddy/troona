/*
 * Copyright 2024 Don Freddy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.jetbrains.kotlin.android)
  alias(libs.plugins.hilt.android)
  kotlin("kapt")
}

android {
  namespace = "com.donfreddy.troona"
  compileSdk = 34

  defaultConfig {
    applicationId = "com.donfreddy.troona"
    minSdk = 24
    targetSdk = 34
    versionCode = 1
    versionName = "1.0.0" // X.Y.Z; X = Major, Y = minor, Z = Patch level

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    vectorDrawables {
      useSupportLibrary = true
    }
  }

  buildTypes {
    release {
      isMinifyEnabled = true
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }
  kotlinOptions {
    jvmTarget = "1.8"
  }
  buildFeatures {
    compose = true
  }
  composeOptions {
    kotlinCompilerExtensionVersion = "1.5.1"
  }
  packaging {
    resources {
      excludes += "/META-INF/{AL2.0,LGPL2.1}"
    }
  }
}

kapt {
  correctErrorTypes = true
  useBuildCache = true
}

dependencies {
  implementation(project(":core:common"))
  implementation(project(":core:ui"))

  implementation(project(":feature:home"))
  implementation(project(":feature:favorites"))
  implementation(project(":feature:playlists"))
  implementation(project(":feature:settings"))
  implementation(project(":feature:player"))

  implementation(libs.hilt.android)
  implementation(libs.hilt.nav.compose)
  kapt(libs.hilt.android.compiler)
  implementation(libs.timber)
  implementation(libs.constraintlayout.compose)
  implementation(libs.splashscreen)
  implementation(libs.androidx.compose.material)
  implementation(libs.google.android.material)
  implementation(libs.tracing.ktx)
  implementation(libs.accompanist.permissions)
  implementation(libs.accompanist.navigation.material)

  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.lifecycle.runtime.ktx)
  implementation(libs.androidx.activity.compose)
  implementation(platform(libs.androidx.compose.bom))
  implementation(libs.androidx.ui)
  implementation(libs.androidx.ui.graphics)
  implementation(libs.androidx.ui.tooling.preview)
  testImplementation(libs.junit)
  androidTestImplementation(libs.androidx.junit)
  androidTestImplementation(libs.androidx.espresso.core)
  androidTestImplementation(platform(libs.androidx.compose.bom))
  androidTestImplementation(libs.androidx.ui.test.junit4)
  debugImplementation(libs.androidx.ui.tooling)
  debugImplementation(libs.androidx.ui.test.manifest)
}