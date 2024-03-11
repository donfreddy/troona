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

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.SwipeableState
import androidx.compose.material.Text
import androidx.compose.material.swipeable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ExperimentalMotionApi
import androidx.constraintlayout.compose.MotionLayout
import androidx.constraintlayout.compose.MotionScene
import androidx.constraintlayout.compose.layoutId
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import com.donfreddy.troona.R
import com.donfreddy.troona.core.designsystem.component.TroonaTopBar
import com.donfreddy.troona.core.permission.PermissionContent
import com.donfreddy.troona.feature.player.FullPlayer
import com.donfreddy.troona.feature.player.mini.MiniPlayer
import com.donfreddy.troona.navigation.TopLevelDestination
import com.donfreddy.troona.navigation.TroonaNavHost
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus


@OptIn(ExperimentalMaterialApi::class, ExperimentalPermissionsApi::class)
@Composable
fun TroonaApp(
  appState: TroonaAppState = rememberTroonaAppState(),
) {
  when (appState.permissionState.status) {
    PermissionStatus.Granted -> {
      TroonaAppContent(appState = appState)
    }

    is PermissionStatus.Denied -> {
      PermissionContent(
        permissionState = appState.permissionState,
        isPermissionRequested = appState.isPermissionRequested
      )
    }
  }
}

@OptIn(ExperimentalMotionApi::class, ExperimentalMaterialApi::class)
@Composable
fun TroonaAppContent(
  appState: TroonaAppState, modifier: Modifier = Modifier, context: Context = LocalContext.current
) {
  // Initialize motion scene content from json5 file
  val motionSceneContent = remember {
    context.resources.openRawResource(R.raw.motion_screne).readBytes().decodeToString()
  }

  //Todo: fix player swipe issue
  MotionLayout(
    modifier = modifier
      .fillMaxSize()
      .safeContentPadding(),
    motionScene = MotionScene(content = motionSceneContent),
    progress = appState.motionProgress
  ) {
    // Top bar
    Box(modifier = Modifier.layoutId("topBar")) {
      TroonaTopBar(modifier = modifier, searchWidgetState = {})
    }

    // Main content
    Box(
      modifier = Modifier
        .background(MaterialTheme.colors.background)
        .layoutId("content")
    ) {
      TroonaNavHost(
        navController = appState.navController,
        onNavigateToPlayer = appState::openPlayer,
      )
    }

    // Mini player
    Box(modifier = Modifier
      .background(MaterialTheme.colors.background)
      .layoutId("miniPlayer")) {
      MiniPlayer(
        modifier = Modifier.playerSwipe(
          swipeableState = appState.swipeableState,
          anchors = appState.anchors,
        ), onNavigateToPlayer = appState::openPlayer
      )
    }

    // Full player
    Box(
      modifier = Modifier
        .background(MaterialTheme.colors.background)
        .layoutId("fullPlayer")
    ) {
      FullPlayer(
        modifier = Modifier.playerSwipe(
          swipeableState = appState.swipeableState,
          anchors = appState.anchors,
        ),
      )
    }

    // Bottom navigation
    Box(modifier = Modifier.layoutId("navigationBar")) {
      TroonaBottomBar(
        destinations = appState.topLevelDestinations,
        currentDestination = appState.currentDestination,
        onNavigateToDestination = appState::navigateToTopLevelDestination,
      )
    }
  }
}

@Composable
fun TroonaBottomBar(
  destinations: List<TopLevelDestination>,
  currentDestination: NavDestination?,
  onNavigateToDestination: (TopLevelDestination) -> Unit,
  modifier: Modifier = Modifier,
) {
  AnimatedVisibility(
    visible = currentDestination != null,
    enter = slideInVertically(initialOffsetY = { it }),
    exit = slideOutVertically(targetOffsetY = { it }),
    modifier = modifier
  ) {
    BottomNavigation(
      backgroundColor = MaterialTheme.colors.background,
      contentColor = Color.White,
      elevation = 0.dp, // Todo: add elevation if mini player is not visible
    ) {
      destinations.forEach { destination ->
        val isSelected = currentDestination.isTopLevelDestinationInHierarchy(destination)
        BottomNavigationItem(icon = {
          Icon(
            painter = painterResource(id = if (isSelected) destination.selectedIcon.resourceId else destination.unselectedIcon.resourceId),
            contentDescription = stringResource(id = destination.titleResource)
          )
        },
          label = {
            Text(
              text = stringResource(id = destination.titleResource),
              fontSize = 14.sp,
              fontWeight = FontWeight.SemiBold,
              softWrap = false
            )
          },
          selectedContentColor = MaterialTheme.colors.primary,
          unselectedContentColor = MaterialTheme.colors.onBackground,
          alwaysShowLabel = false,
          selected = isSelected,
          onClick = { onNavigateToDestination(destination) })
      }
    }
  }
}

private fun NavDestination?.isTopLevelDestinationInHierarchy(destination: TopLevelDestination) =
  this?.hierarchy?.any { it.route?.contains(destination.name, true) ?: false } ?: false

/**
 * A [Modifier] that adds swipeable behavior to a player view.
 * Todo: migrate deprecated swipeable to use new swipeable
 */
@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterialApi::class)
private fun Modifier.playerSwipe(
  swipeableState: SwipeableState<Int>, anchors: Map<Float, Int>
): Modifier = this.then(
  swipeable(state = swipeableState,
    anchors = anchors,
    orientation = Orientation.Vertical,
    thresholds = { _, _ ->
      FractionalThreshold(SwipeFraction)
    }),
)

private const val SwipeFraction = 0.3f