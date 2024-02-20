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

package com.donfreddy.troona

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.KaptExtensionConfig
import org.jetbrains.kotlin.gradle.dsl.KaptJavacOption
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions

/**
 * Configure base Kotlin with Android options
 */
internal fun Project.configureKotlinAndroid(
  commonExtension: CommonExtension<*, *, *, *, *>,
) {
  with(commonExtension) {
    compileSdk = libs.findVersion("android.compileSdk").get().toString().toInt()

    defaultConfig {
      minSdk = 21
      testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
      sourceCompatibility = JavaVersion.VERSION_1_8
      targetCompatibility = JavaVersion.VERSION_1_8
      isCoreLibraryDesugaringEnabled = true
    }

    kotlinOptions {
      jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
  }

  dependencies {
    add("coreLibraryDesugaring", libs.findLibrary("android.desugarJdkLibs").get())
  }
}


private fun CommonExtension<*, *, *, *, *>.kotlinOptions(block: KotlinJvmOptions.() -> Unit) =
  (this as ExtensionAware).extensions.configure("kotlinOptions", block)

private fun CommonExtension<*, *, *, *, *>.kapt(block: KaptExtensionConfig.() -> Unit) =
  (this as ExtensionAware).extensions.configure("kapt", block)