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

package com.donfreddy.troona.core.designsystem.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.donfreddy.troona.core.designsystem.R
import com.donfreddy.troona.core.designsystem.icon.TroonaIcons

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TroonaTopBar(
  hasLogo: Boolean = false,
  isContextual: Boolean = false,
  searchWidgetState: () -> Unit,
  onBackClick: () -> Unit = {},
  onMenuClick: () -> Unit = {},
  scrollBehavior: TopAppBarScrollBehavior,
  modifier: Modifier = Modifier
) {
  val backgroundColor = if (isContextual) Color.Red else Color.White
  val contentColor = if (isContextual) Color.White else Color.DarkGray

  TopAppBar(
    scrollBehavior = scrollBehavior,
    /* colors = TopAppBarColors(
       containerColor = contentColor,
       scrolledContainerColor = backgroundColor,
       navigationIconContentColor = backgroundColor,
       titleContentColor = backgroundColor,
       actionIconContentColor = backgroundColor,
     ),*/
    navigationIcon = {
      if (!hasLogo) {
        IconButton(onClick = onBackClick) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Localized description"
          )

        }
      }
    },
    title = {
      if (hasLogo) {
        Row(
          modifier = modifier.fillMaxSize(),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            imageVector = TroonaIcons.Star.imageVector,
            contentDescription = null
          )
          Spacer(modifier = modifier.width(4.dp))
          Text(
            text = stringResource(id = R.string.core_designsystem_app_name),
            style = MaterialTheme.typography.headlineMedium.copy(
              fontWeight = FontWeight.W700
            )
          )
        }
      }
    },
    //backgroundColor = MaterialTheme.colors.background,
    //elevation = 0.dp,
    //modifier = modifier.statusBarsPadding(),
    actions = {
      IconButton(
        onClick = { searchWidgetState() },
        modifier = Modifier
      ) {
        Icon(
          imageVector = TroonaIcons.Search.imageVector,
          contentDescription = null
        )
      }
      if (!hasLogo) {
        IconButton(
          onClick = onMenuClick,
          modifier = Modifier
        ) {
          Icon(
            painter = painterResource(id = TroonaIcons.MoreHorizontal.resourceId),
            contentDescription = "More",
          )
        }
      }
      if (isContextual) {
        IconButton(onClick = {}) {
          Icon(
            imageVector = Icons.Default.Share,
            contentDescription = "Share",
            tint = Color.White
          )
        }
      }
    },
  )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContextualActionBar(
  selectedCount: Int,
  onClearSelection: () -> Unit,
  onDelete: () -> Unit
) {
  TopAppBar(
    title = { Text("$selectedCount selected") },
    navigationIcon = {
      IconButton(onClick = onClearSelection) {
        Icon(Icons.Default.Close, contentDescription = "Clear selection")
      }
    },
    actions = {
      IconButton(onClick = onDelete) {
        Icon(Icons.Default.Delete, contentDescription = "Delete")
      }
    },
    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Gray)
  )
}