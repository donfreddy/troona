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
  alias(libs.plugins.troona.android.application)
  alias(libs.plugins.troona.android.application.compose)
  alias(libs.plugins.troona.android.hilt)
  alias(libs.plugins.troona.android.application.firebase)
}

android {
  namespace = "com.donfreddy.troona"

  defaultConfig {
    applicationId = "com.donfreddy.troona"
    versionCode = 1
    versionName = "1.0.0" // X.Y.Z; X = Major, Y = minor, Z = Patch level
  }

  buildTypes {
    getByName("release") {
      isMinifyEnabled = true
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
  }

  packaging {
    resources {
      excludes += "/META-INF/{AL2.0,LGPL2.1}"
    }
  }
}

dependencies {
  implementation(projects.core.common)
  implementation(projects.core.data)
  implementation(projects.core.datastore)
  implementation(projects.core.domain)
  implementation(projects.core.ui)
  implementation(projects.core.designsystem)
  implementation(projects.core.model)
  implementation(projects.core.permission)

  implementation(projects.feature.home)
  implementation(projects.feature.favorites)
  implementation(projects.feature.playlists)
  implementation(projects.feature.settings)
  implementation(projects.feature.player)
  implementation(projects.feature.search)

  implementation(libs.timber)
  implementation(libs.androidx.activity.compose)
  implementation(libs.constraintlayout.compose)
  implementation(libs.splashscreen)
  implementation(libs.androidx.compose.material)
  implementation(libs.google.android.material)
  implementation(libs.tracing.ktx)
  implementation(libs.accompanist.navigation.material)
  implementation(libs.kotlinx.coroutines.guava)

  testImplementation(libs.junit)
  androidTestImplementation(libs.androidx.ui.test.junit4)
  /*implementation(libs.androidx.core.ktx)
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
  debugImplementation(libs.androidx.ui.test.manifest)*/
}