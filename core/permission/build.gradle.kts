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
  alias(libs.plugins.troona.android.library)
  alias(libs.plugins.troona.android.library.compose)
}

android {
  namespace = "com.donfreddy.troona.core.permission"
}

dependencies {
  implementation(project.projects.core.designsystem)
  implementation(libs.androidx.core.ktx)
  api(libs.accompanist.permissions)
}