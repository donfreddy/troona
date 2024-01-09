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

package com.donfreddy.troona.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.consumedWindowInsets
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import com.donfreddy.troona.ui.navigation.BottomNavItem
import com.donfreddy.troona.ui.navigation.MainScreens

@Composable
fun TroonaBottomBar(navController: NavController, tabs: List<BottomNavItem>) {

  // State of bottomBar, set state to false, if current page route is a detail page
  val bottomBarState = rememberSaveable { (mutableStateOf(true)) }

  // Subscribe to navBackStackEntry, required to get current route
  val navBackStackEntry by navController.currentBackStackEntryAsState()
  val currentDestination = navBackStackEntry?.destination

  when (currentDestination?.route) {
    MainScreens.Home.route -> bottomBarState.value = true
    MainScreens.Favorite.route -> bottomBarState.value = true
    MainScreens.Playlist.route -> bottomBarState.value = true
    MainScreens.Setting.route -> bottomBarState.value = true
    // CineyScreen.MovieDetail.route -> bottomBarState.value = false
  }

  AnimatedVisibility(
    visible = bottomBarState.value,
    enter = slideInVertically(initialOffsetY = { it }),
    exit = slideOutVertically(targetOffsetY = { it }),
  ) {
    BottomNavigation(
      backgroundColor = Color.White,
      contentColor = Color.White,
      elevation = 6.dp,
      modifier = Modifier.systemBarsPadding()
    ) {
      tabs.forEach { tab ->
        val isSelected = currentDestination?.hierarchy?.any { it.route == tab.route } == true
        BottomNavigationItem(icon = {
          Icon(
            painter = painterResource(id = if (isSelected) tab.selectedIcon else tab.unselectedIcon),
            contentDescription = tab.route
          )
        },
          label = {
            Text(
              text = stringResource(id = tab.title),
              fontSize = 11.sp,
              softWrap = false
            )
          },
          selectedContentColor = MaterialTheme.colors.primary,
          unselectedContentColor = Color.Red,
          alwaysShowLabel = false,
          selected = isSelected,
          onClick = {
            navController.navigate(tab.route) {
              navController.graph.startDestinationRoute?.let { route ->
                popUpTo(route) {
                  saveState = true
                }
              }
              launchSingleTop = true
              restoreState = true
            }
          })
      }
    }
  }
}