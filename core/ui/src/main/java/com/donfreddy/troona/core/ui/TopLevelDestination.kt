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

package com.donfreddy.troona.core.ui

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

/**
 * Type for the top level destinations in the application. Each of these destinations
 * can contain one or more screens (based on the window size). Navigation from one screen to the
 * next within a single destination will be handled directly in composable.
 */
enum class TopLevelDestination(
  @StringRes val titleRes: Int,
  @DrawableRes val selectedIconRes: Int,
  @DrawableRes val unselectedIconRes: Int,
) {
  HOME(
    titleRes = R.string.home_title,
    selectedIconRes = R.drawable.ic_home,
    unselectedIconRes = R.drawable.ic_outline_home,
  ),
  FAVORITES(
    titleRes = R.string.favorite_title,
    selectedIconRes = R.drawable.ic_favorite,
    unselectedIconRes = R.drawable.ic_outline_favorite,
  ),
  PLAYLISTS(
    titleRes = R.string.playlists_title,
    selectedIconRes = R.drawable.ic_playlist,
    unselectedIconRes = R.drawable.ic_playlist,
  ),
  SETTINGS(
    titleRes = R.string.settings_title,
    selectedIconRes = R.drawable.ic_settings,
    unselectedIconRes = R.drawable.ic_outline_settings,
  )
}