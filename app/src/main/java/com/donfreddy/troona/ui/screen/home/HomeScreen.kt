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

package com.donfreddy.troona.ui.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.donfreddy.troona.R
import com.donfreddy.troona.ui.navigation.TroonaNavAction
import com.donfreddy.troona.ui.screen.settings.SettingsScreen
import com.donfreddy.troona.ui.theme.TroonaColor
import com.donfreddy.troona.ui.theme.TroonaStyle
import com.donfreddy.troona.util.SearchWidgetState
import com.gandiva.neumorphic.LightSource
import com.gandiva.neumorphic.neu
import com.gandiva.neumorphic.shape.Flat
import com.gandiva.neumorphic.shape.Oval

@Composable
fun HomeScreen(
  modifier: Modifier = Modifier,
  //navAction: TroonaNavAction,
) {
  Column(
    modifier = Modifier
      .fillMaxSize()
      .wrapContentSize(Alignment.Center)
  ) {
    Text(
      text = "Home Screen",
      modifier = Modifier.align(Alignment.CenterHorizontally),
      textAlign = TextAlign.Center,
      style = MaterialTheme.typography.h6
    )
  }
}

@Composable
fun HomeTopBar(searchWidgetState: () -> Unit) {

  TopAppBar(
    title = { Text(text = stringResource(id = R.string.my_music)) },
    navigationIcon = {
      IconButton(onClick = { }) {
        Icon(Icons.Default.Menu, contentDescription = null)
      }
    },
    actions = {
      IconButton(
        onClick = { searchWidgetState() }, Modifier.neu(
          lightShadowColor = TroonaColor.lightShadow(),
          darkShadowColor = TroonaColor.darkShadow(),
          //shadowElevation =  6.dp,
          lightSource = LightSource.LEFT_TOP,
          shape = Flat(Oval),
        )
      ) {
        Icon(Icons.Default.Search, contentDescription = null)
      }
    })
}

@Composable
fun HomeSearchTopBar(
  searchTextState: String,
  onSearchTextChange: (String) -> Unit,
  onSearchWidgetClose: () -> Unit,
) {
  Surface(
    modifier = Modifier
      .fillMaxWidth()
      .height(56.dp)
  ) {
    TextField(
      value = "",
      onValueChange = onSearchTextChange,
      singleLine = true,
      maxLines = 1,
      label = { Text(text = stringResource(id = R.string.search)) },
      leadingIcon = {
        Icon(
          Icons.Default.Search,
          contentDescription = null,
          modifier = Modifier.padding(horizontal = 12.dp)
        )
      },
      trailingIcon = {
        IconButton(onClick = {
          if (searchTextState.isNotEmpty() || searchTextState.isNotBlank()) {
            // searchTextState = searchTextState
          } else {
            onSearchWidgetClose()
          }
        }) {
          Icon(
            imageVector = Icons.Rounded.Close, contentDescription = "Close Icon", tint = Color.White
          )
        }
      },
      modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp),
      keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
      keyboardActions = KeyboardActions(onSearch = { onSearchWidgetClose() }),
    )
  }
}

@Preview(
  showBackground = true,
  name = "Home screen"
)
@Composable
fun HomeScreenPreview() {
  HomeScreen()
}