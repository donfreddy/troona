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

package com.donfreddy.troona.ui

import android.Manifest
import android.content.res.Configuration
import android.os.Build
import androidx.compose.animation.core.tween
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SwipeableState
import androidx.compose.material.rememberSwipeableState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import androidx.tracing.trace
import com.donfreddy.troona.feature.favorites.navigation.navigateToFavorites
import com.donfreddy.troona.feature.home.navigation.navigateToHome
import com.donfreddy.troona.feature.playlists.navigation.navigateToPlaylists
import com.donfreddy.troona.feature.settings.navigation.navigateToSettings
import com.donfreddy.troona.navigation.TopLevelDestination
import com.donfreddy.troona.navigation.TopLevelDestination.FAVORITES
import com.donfreddy.troona.navigation.TopLevelDestination.HOME
import com.donfreddy.troona.navigation.TopLevelDestination.PLAYLISTS
import com.donfreddy.troona.navigation.TopLevelDestination.SETTINGS
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.max
import kotlin.math.min

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun rememberTroonaAppState(
  navController: NavHostController = rememberNavController(),
  swipeableState: SwipeableState<Int> = rememberSwipeableState(0, tween()),
  coroutineScope: CoroutineScope = rememberCoroutineScope(),
  density: Density = LocalDensity.current,
  configuration: Configuration = LocalConfiguration.current
): TroonaAppState {
  val screenHeight = with(density) { configuration.screenHeightDp.dp.toPx() }
  val swipeAreaHeight = screenHeight - SwipeAreaOffset

  return remember(navController, swipeableState, coroutineScope) {
    TroonaAppState(
      navController = navController,
      swipeableState = swipeableState,
      swipeAreaHeight = swipeAreaHeight,
      coroutineScope = coroutineScope
    )
  }
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalPermissionsApi::class)
@Stable
class TroonaAppState(
  val navController: NavHostController,
  val swipeableState: SwipeableState<Int>,
  val swipeAreaHeight: Float,
  val coroutineScope: CoroutineScope,
) {
  val currentDestination: NavDestination?
    @Composable get() = navController.currentBackStackEntryAsState().value?.destination

  // Swipeable State
  val anchors = mapOf(0f to 0, -swipeAreaHeight to 1)
  private val swipeProgress @Composable get() = swipeableState.offset.value / -swipeAreaHeight
  val motionProgress @Composable get() = max(0f, min(swipeProgress, 1f))
  val isPlayerOpened @Composable get() = swipeableState.currentValue == 1

  // Permission State
  val permissionState: PermissionState
    @Composable get() = rememberTroonaPermissionState { isPermissionRequested = true }
  var isPermissionRequested by mutableStateOf(false)
    private set

  val topLevelDestinations: List<TopLevelDestination> = TopLevelDestination.entries

  /**
   * UI logic for navigating to a top level destination in the app. Top level destinations have
   * only one copy of the destination of the back stack, and save and restore state whenever you
   * navigate to and from it.
   *
   * @param topLevelDestination: The destination the app needs to navigate to.
   */
  fun navigateToTopLevelDestination(topLevelDestination: TopLevelDestination) {
    trace("Navigating to ${topLevelDestination.name}") {
      val topLevelNavOptions = navOptions {
        // Pop up to the start destination of the graph to
        // avoid building up a large stack of destinations
        // on the back stack as users select items
        popUpTo(navController.graph.findStartDestination().id) {
          saveState = true
        }
        // Avoid multiple copies of the same destination when
        // reelecting the same item
        launchSingleTop = true
        // Restore state when reelecting a previously selected item
        restoreState = true
      }

      when (topLevelDestination) {
        HOME -> navController.navigateToHome(topLevelNavOptions)
        FAVORITES -> navController.navigateToFavorites(topLevelNavOptions)
        PLAYLISTS -> navController.navigateToPlaylists(topLevelNavOptions)
        SETTINGS -> navController.navigateToSettings(topLevelNavOptions)
      }
    }
  }

  fun openPlayer() = coroutineScope.launch { swipeableState.animateTo(1) }
  fun closePlayer() = coroutineScope.launch { swipeableState.animateTo(0) }
  fun onBackClick() = navController.popBackStack()
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun rememberTroonaPermissionState(
  onPermissionResult: (Boolean) -> Unit = {}
) = rememberPermissionState(
  permission = TroonaAudioPermission,
  onPermissionResult = onPermissionResult
)

private val TroonaAudioPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
  Manifest.permission.READ_MEDIA_AUDIO
} else {
  Manifest.permission.READ_EXTERNAL_STORAGE
}

private const val SwipeAreaOffset = 400