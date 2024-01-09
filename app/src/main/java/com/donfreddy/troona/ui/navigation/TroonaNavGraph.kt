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

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.donfreddy.troona.util.WindowSize

@Composable
fun TroonaNavGraph(
  navController: NavHostController = rememberNavController(),
  windowSize: WindowSize,
  startDestination: String = TroonaScreens.Main.route,
) {
  val navAction = remember(navController) { TroonaNavAction(navController) }

  NavHost(
    navController = navController,
    startDestination = startDestination,
  ) {
    //Todo: Splash screen should be implemented here

    // Main screens (home, favorite, playlist, setting)
    navigation(
      route = TroonaScreens.Main.route,
      startDestination = MainScreens.Home.route,
    ) {
      bottomNaGraph(
        navController = navController,
        windowSize = windowSize,
        navAction = navAction,
      )
    }

    // Other screens (search, player)
  }
}
