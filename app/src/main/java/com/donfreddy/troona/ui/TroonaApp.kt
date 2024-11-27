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

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.SwipeableState
import androidx.compose.material.swipeable
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.ExperimentalMotionApi
import androidx.constraintlayout.compose.MotionLayout
import androidx.constraintlayout.compose.layoutId
import androidx.media3.common.util.UnstableApi
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import com.donfreddy.troona.core.permission.PermissionContent
import com.donfreddy.troona.core.ui.controllers.SnackBarController
import com.donfreddy.troona.core.ui.events.ObserveAsEvents
import com.donfreddy.troona.feature.player.FullPlayer
import com.donfreddy.troona.feature.player.mini.MiniPlayer
import com.donfreddy.troona.navigation.TopLevelDestination
import com.donfreddy.troona.navigation.TroonaNavHost
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class, ExperimentalPermissionsApi::class)
@UnstableApi
@Composable
fun TroonaApp(
  onSetSystemBarsLightIcons: () -> Unit,
  onResetSystemBarsIcons: () -> Unit,
  appState: TroonaAppState = rememberTroonaAppState(),
) {
  when (appState.permissionState.status) {
    PermissionStatus.Granted -> {
      val snackBarHostState = remember {
        SnackbarHostState()
      }

      ObserveAsEvents(
        flow = SnackBarController.events,
        key1 = snackBarHostState,
      ) { event ->
        appState.coroutineScope.launch {
          snackBarHostState.currentSnackbarData?.dismiss()

          val result = snackBarHostState.showSnackbar(
            message = event.message,
            actionLabel = event.action?.name,
            duration = SnackbarDuration.Long
          )

          if (result == SnackbarResult.ActionPerformed) {
            event.action?.action?.invoke()
          }
        }
      }
      Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
      ) { innerPadding ->
        TroonaAppContent(
          appState = appState,
          onSetSystemBarsLightIcons = onSetSystemBarsLightIcons,
          onResetSystemBarsIcons = onResetSystemBarsIcons,
          modifier = Modifier.padding(innerPadding)
        )
      }

    }

    is PermissionStatus.Denied -> {
      PermissionContent(
        permissionState = appState.permissionState,
        isPermissionRequested = appState.isPermissionRequested
      )
    }
  }
}

@OptIn(
  ExperimentalMotionApi::class, ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class
)
@UnstableApi
@Composable
fun TroonaAppContent(
  appState: TroonaAppState,
  onSetSystemBarsLightIcons: () -> Unit,
  onResetSystemBarsIcons: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val topLevelDestinations = appState.topLevelDestinations
  val currentDestination = appState.currentDestination


  val statusBarsHeight: Dp
  val navigationBarsHeight: Dp
  with(LocalDensity.current) {
    statusBarsHeight = WindowInsets.systemBars.getTop(this).toDp()
    navigationBarsHeight = WindowInsets.navigationBars.getBottom(this).toDp()
  }

  val startConstraintSet = ConstraintSet {
    val content = createRefFor(ContentId)
    val miniPlayer = createRefFor(MiniPlayerId)
    val fullPlayer = createRefFor(FullPlayerId)
    val navigationBar = createRefFor(NavigationBarId)

    constrain(content) {
      height = Dimension.fillToConstraints
      width = Dimension.fillToConstraints
      top.linkTo(parent.top)
      start.linkTo(parent.start)
      end.linkTo(parent.end)
      bottom.linkTo(parent.bottom)
    }

    constrain(miniPlayer) {
      start.linkTo(parent.start)
      end.linkTo(parent.end)
      bottom.linkTo(navigationBar.top)
    }

    constrain(fullPlayer) {
      top.linkTo(navigationBar.top)
      start.linkTo(parent.start)
      end.linkTo(parent.end)
    }

    constrain(navigationBar) {
      start.linkTo(parent.start)
      end.linkTo(parent.end)
      bottom.linkTo(parent.bottom)
    }
  }
  val endConstraintSet = ConstraintSet {
    val content = createRefFor(ContentId)
    val miniPlayer = createRefFor(MiniPlayerId)
    val fullPlayer = createRefFor(FullPlayerId)
    val navigationBar = createRefFor(NavigationBarId)

    constrain(content) {
      height = Dimension.fillToConstraints
      width = Dimension.fillToConstraints
      top.linkTo(parent.top)
      start.linkTo(parent.start)
      end.linkTo(parent.end)
      bottom.linkTo(navigationBar.top)
    }

    constrain(miniPlayer) {
      top.linkTo(fullPlayer.top)
      start.linkTo(parent.start)
      end.linkTo(parent.end)
    }

    constrain(fullPlayer) {
      top.linkTo(parent.top)
      start.linkTo(parent.start)
      end.linkTo(parent.end)
      bottom.linkTo(content.bottom)
    }

    constrain(navigationBar) {
      top.linkTo(parent.bottom)
      start.linkTo(parent.start)
      end.linkTo(parent.end)
    }
  }

  MotionLayout(
    start = startConstraintSet,
    end = endConstraintSet,
    modifier = Modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
      .padding(
        bottom = if (topLevelDestinations.any {
            currentDestination.isTopLevelDestinationInHierarchy(
              it
            )
          })  0.dp else if (appState.motionProgress > 0.1f) 0.dp else navigationBarsHeight
      ),
    progress = appState.motionProgress,
  ) {

    // Main content
    Box(
      modifier = Modifier
        .background(MaterialTheme.colorScheme.background)
        .layoutId(ContentId)
    ) {
      TroonaNavHost(
        //navController = appState.navController,
        //onNavigateToArtist = appState::navigateToArtist,
        // onSetSystemBarsLightIcons = onSetSystemBarsLightIcons,
        // onResetSystemBarsIcons = onResetSystemBarsIcons,
        appState = appState
      )
    }

    // Mini player
    Box(
      modifier = Modifier
        .background(MaterialTheme.colorScheme.background)
        .layoutId(MiniPlayerId)
    ) {
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
        .background(MaterialTheme.colorScheme.background)
        .layoutId(FullPlayerId)
        .alpha(appState.motionProgress * 10f)
    ) {
      FullPlayer(
        modifier = Modifier.playerSwipe(
          swipeableState = appState.swipeableState,
          anchors = appState.anchors,
        ),
        isPlayerOpened = appState.isPlayerOpened,
        onSetSystemBarsLightIcons = onSetSystemBarsLightIcons,
        onResetSystemBarsIcons = onResetSystemBarsIcons,
      )
    }

    // Bottom navigation
    Box(modifier = Modifier.layoutId(NavigationBarId)) {
      TroonaBottomBar(
        destinations = topLevelDestinations,
        currentDestination = currentDestination,
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
  modifier: Modifier = Modifier.systemBarsPadding(),
) {
  AnimatedVisibility(
    visible = destinations.any { currentDestination.isTopLevelDestinationInHierarchy(it) },
    enter = slideInVertically(initialOffsetY = { it }),
    exit = slideOutVertically(targetOffsetY = { it }),
  ) {
    NavigationBar(
      containerColor = MaterialTheme.colorScheme.background,
      contentColor = Color.White,
      //tonalElevation = 0.dp, // Todo: add elevation if mini player is not visible
    ) {
      destinations.forEach { destination ->
        val isSelected = currentDestination.isTopLevelDestinationInHierarchy(destination)
        NavigationBarItem(icon = {
          Icon(
            painter = painterResource(id = if (isSelected) destination.selectedIcon.resourceId else destination.unselectedIcon.resourceId),
            contentDescription = stringResource(id = destination.titleResource)
          )
        }, label = {
          Text(
            text = stringResource(id = destination.titleResource),
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            softWrap = false
          )
        }, colors = NavigationBarItemColors(
          selectedIconColor = MaterialTheme.colorScheme.primary,
          selectedTextColor = MaterialTheme.colorScheme.primary,
          selectedIndicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
          unselectedIconColor = MaterialTheme.colorScheme.onSurface,
          unselectedTextColor = MaterialTheme.colorScheme.onSurface,
          disabledIconColor = MaterialTheme.colorScheme.onSurface,
          disabledTextColor = MaterialTheme.colorScheme.onSurface,
        ),
          //alwaysShowLabel = false,
          selected = isSelected,
          //modifier = modifier,
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
fun Modifier.playerSwipe(
  swipeableState: SwipeableState<Int>, anchors: Map<Float, Int>
): Modifier = this.then(
  swipeable(state = swipeableState,
    anchors = anchors,
    orientation = Orientation.Vertical,
    thresholds = { _, _ ->
      FractionalThreshold(SwipeFraction)
    }),
)

private const val ContentId = "content"
private const val MiniPlayerId = "miniPlayer"
private const val FullPlayerId = "fullPlayer"
private const val NavigationBarId = "navigationBar"
private const val SwipeFraction = 0.3f