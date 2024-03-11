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

package com.donfreddy.troona.navigation

import androidx.annotation.StringRes
import com.donfreddy.troona.feature.home.R as HomeR
import com.donfreddy.troona.feature.favorites.R as FavoriteR
import com.donfreddy.troona.feature.playlists.R as PlaylistR
import com.donfreddy.troona.feature.settings.R as SettingsR
import com.donfreddy.troona.core.designsystem.icon.Icon.DrawableResourceIcon
import com.donfreddy.troona.core.designsystem.icon.TroonaIcons

/**
 * Type for the top level destinations in the application. Each of these destinations
 * can contain one or more screens (based on the window size). Navigation from one screen to the
 * next within a single destination will be handled directly in composable.
 */
enum class TopLevelDestination(
  val selectedIcon: DrawableResourceIcon,
  val unselectedIcon: DrawableResourceIcon,
  @StringRes val titleResource: Int
) {
  HOME(
    selectedIcon = TroonaIcons.Home,
    unselectedIcon = TroonaIcons.HomeBorder,
    titleResource = HomeR.string.feature_home_title
  ),
  FAVORITES(
    selectedIcon = TroonaIcons.Favorite,
    unselectedIcon = TroonaIcons.FavoriteBorder,
    titleResource = FavoriteR.string.feature_favorites_title
  ),
  PLAYLISTS(
    selectedIcon = TroonaIcons.Playlist,
    unselectedIcon = TroonaIcons.Playlist,
    titleResource = PlaylistR.string.feature_playlists_title
  ),
  SETTINGS(
    selectedIcon = TroonaIcons.Settings,
    unselectedIcon = TroonaIcons.SettingsBorder,
    titleResource = SettingsR.string.feature_settings_title
  )
}