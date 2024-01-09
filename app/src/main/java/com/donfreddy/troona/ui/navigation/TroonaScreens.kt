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

import androidx.navigation.NavHostController
import com.donfreddy.troona.ui.TroonaApp

/**
 * Screens used in the [TroonaApp].
 */
sealed class TroonaScreens(val route: String) {
  data object Splash : TroonaScreens("splash")
  data object Main : TroonaScreens("main")
}

sealed class MainScreens(val route: String) {
  data object Home : MainScreens("home")
  data object Favorite : MainScreens("favorite")
  data object Playlist : MainScreens("playlist")
  data object Setting : MainScreens("setting")
}

/**
 * Models the navigation actions in the app.
 */
class TroonaNavAction(navController: NavHostController) {

  val navigateBack: () -> Unit = {
    navController.navigateUp()
  }
}
