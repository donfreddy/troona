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

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy

@Composable
fun TroonaBottomBar(
  destinations: List<TopLevelDestination>,
  currentDestination: NavDestination?,
  onNavigateToDestination: (TopLevelDestination) -> Unit,
  modifier: Modifier = Modifier,
) {
  AnimatedVisibility(
    visible = currentDestination == null,
    enter = slideInVertically(initialOffsetY = { it }),
    exit = slideOutVertically(targetOffsetY = { it }),
  ) {
    BottomNavigation(
      backgroundColor = MaterialTheme.colors.background,
      contentColor = Color.White,
      elevation = 6.dp,
      //modifier = modifier.systemBarsPadding()
    ) {
      destinations.forEach { destination ->
        val isSelected = currentDestination.isTopLevelDestinationInHierarchy(destination)
        BottomNavigationItem(icon = {
          Icon(
            painter = painterResource(id = if (isSelected) destination.selectedIconRes else destination.unselectedIconRes),
            contentDescription = null
          )
        },
          label = {
            Text(
              text = stringResource(id = destination.titleRes),
              fontSize = 14.sp,
              fontWeight = FontWeight.SemiBold,
              softWrap = false
            )
          },
          selectedContentColor = MaterialTheme.colors.primary,
          unselectedContentColor = MaterialTheme.colors.onBackground,
          alwaysShowLabel = false,
          selected = isSelected,
          onClick = { onNavigateToDestination(destination) }
        )
      }
    }
  }
}

private fun NavDestination?.isTopLevelDestinationInHierarchy(destination: TopLevelDestination) =
  this?.hierarchy?.any { it.route?.contains(destination.name, true) ?: false } ?: false