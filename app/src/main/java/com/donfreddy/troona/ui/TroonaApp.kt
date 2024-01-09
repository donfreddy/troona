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

package com.donfreddy.troona.ui

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.donfreddy.troona.ui.components.TroonaBottomBar
import com.donfreddy.troona.ui.navigation.BottomNavItem
import com.donfreddy.troona.ui.navigation.TroonaNavGraph
import com.donfreddy.troona.ui.theme.TroonaTheme
import com.donfreddy.troona.util.WindowSize
import timber.log.Timber

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun TroonaApp(windowSize: WindowSize) {
  TroonaTheme {

    val navController = rememberNavController()

    val tabs = remember {
      listOf(
        BottomNavItem.Home,
        BottomNavItem.Favorite,
        BottomNavItem.Playlist,
        BottomNavItem.Setting,
      )
    }

    Scaffold(
      bottomBar = { TroonaBottomBar(navController = navController, tabs = tabs) },
    ) {

      TroonaNavGraph(
        navController = navController,
        windowSize = windowSize,
      )
    }
  }
}