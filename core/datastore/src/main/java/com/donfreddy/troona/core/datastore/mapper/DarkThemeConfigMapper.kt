/*
 * Copyright 2024 Don Freddy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.donfreddy.troona.core.datastore.mapper

import com.donfreddy.troona.core.datastore.DarkThemeConfigProto
import com.donfreddy.troona.core.model.enums.DarkThemeConfig

internal fun DarkThemeConfig.asDarkThemeConfigProto() = when (this) {
  DarkThemeConfig.SystemDefault -> DarkThemeConfigProto.DARK_THEME_CONFIG_FOLLOW_SYSTEM
  DarkThemeConfig.Light -> DarkThemeConfigProto.DARK_THEME_CONFIG_LIGHT
  DarkThemeConfig.Dark -> DarkThemeConfigProto.DARK_THEME_CONFIG_DARK
}

internal fun DarkThemeConfigProto.asDarkThemeConfig() = when (this) {
  DarkThemeConfigProto.UNRECOGNIZED,
  DarkThemeConfigProto.DARK_THEME_CONFIG_FOLLOW_SYSTEM -> DarkThemeConfig.SystemDefault

  DarkThemeConfigProto.DARK_THEME_CONFIG_LIGHT -> DarkThemeConfig.Light
  DarkThemeConfigProto.DARK_THEME_CONFIG_DARK -> DarkThemeConfig.Dark
}