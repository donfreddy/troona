/*
=============
Author: Don Freddy
Github: https://github.com/donfreddy
Website: https://donfreddy.com
=============
Application: Troona - Music Player
Homepage: https://github.com/donfreddy/troona
License: https://github.com/donfreddy/troona/blob/main/LICENSE
Copyright: © 2023, Don Freddy. All rights reserved.
=============
*/

package com.donfreddy.troona.ui.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.donfreddy.troona.R
import com.donfreddy.troona.ui.screen.favorites.FavoritesScreen
import com.donfreddy.troona.ui.screen.home.HomeScreen
import com.donfreddy.troona.ui.screen.playlists.PlaylistsScreen
import com.donfreddy.troona.ui.screen.settings.SettingsScreen
import com.donfreddy.troona.util.WindowSize

fun NavGraphBuilder.bottomNaGraph(
  navController: NavHostController,
  windowSize: WindowSize,
  navAction: TroonaNavAction,
  onSelected: (Long, NavBackStackEntry) -> Unit = { _, _ -> }
) {
  composable(route = BottomNavItem.Home.route) {
    HomeScreen()
  }

  composable(route = BottomNavItem.Favorite.route) {
    FavoritesScreen()
  }

  composable(route = BottomNavItem.Playlist.route) {
    PlaylistsScreen()
  }

  composable(route = BottomNavItem.Setting.route) {
    SettingsScreen()
  }
}

sealed class BottomNavItem(
  @StringRes val title: Int,
  @DrawableRes val selectedIcon: Int,
  @DrawableRes val unselectedIcon: Int,
  val route: String,
) {

  data object Home : BottomNavItem(
    title = R.string.nav_home,
    selectedIcon = R.drawable.ic_menu_slideshow,
    unselectedIcon = R.drawable.ic_menu_gallery,
    route = MainScreens.Home.route
  )

  data object Favorite : BottomNavItem(
    title = R.string.nav_favorite,
    selectedIcon = R.drawable.ic_menu_slideshow,
    unselectedIcon = R.drawable.ic_menu_gallery,
    route = MainScreens.Favorite.route
  )

  data object Playlist : BottomNavItem(
    title = R.string.nav_playlist,
    selectedIcon = R.drawable.ic_menu_slideshow,
    unselectedIcon = R.drawable.ic_menu_gallery,
    route = MainScreens.Playlist.route
  )

  data object Setting : BottomNavItem(
    title = R.string.nav_setting,
    selectedIcon = R.drawable.ic_menu_slideshow,
    unselectedIcon = R.drawable.ic_menu_gallery,
    route = MainScreens.Setting.route
  )
}