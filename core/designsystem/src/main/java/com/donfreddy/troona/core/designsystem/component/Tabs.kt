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

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Tab
import androidx.compose.material.Text
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.donfreddy.troona.core.designsystem.theme.TroonaTheme

/**
 * Troona tab. Wraps Material [Tab] and shifts text label down.
 *
 * @param selected Whether this tab is selected or not.
 * @param onClick The callback to be invoked when this tab is selected.
 * @param modifier Modifier to be applied to the tab.
 * @param enabled Controls the enabled state of the tab. When `false`, this tab will not be
 * clickable and will appear disabled to accessibility services.
 * @param text The text label content.
 */
@Composable
fun TroonaTab(
  selected: Boolean,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  enabled: Boolean = true,
  text: @Composable () -> Unit,
) {
  Tab(
    selected = selected,
    onClick = onClick,
    modifier = modifier,
    enabled = enabled,
    text = {
      val style = MaterialTheme.typography.button.copy(
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.SemiBold
      )
      ProvideTextStyle(
        value = style,
        content = {
          Box(modifier = Modifier.padding(top = TroonaTabDefaults.TabTopPadding)) {
            text()
          }
        },
      )
    },
  )
}

/**
 * Troona tab row. Wraps Material [ScrollableTabRow].
 *
 * @param selectedTabIndex The index of the currently selected tab.
 * @param modifier Modifier to be applied to the tab row.
 * @param tabs The tabs inside this tab row. Typically this will be multiple [TroonaTab]s. Each element
 * inside this lambda will be measured and placed evenly across the row, each taking up equal space.
 */
@Composable
fun TroonaScrollableTabRow(
  selectedTabIndex: Int,
  modifier: Modifier = Modifier,
  tabs: @Composable () -> Unit,
) {
  ScrollableTabRow(
    selectedTabIndex = selectedTabIndex,
    modifier = modifier,
    edgePadding = 10.dp,
    backgroundColor = MaterialTheme.colors.background,
    indicator = { tabPositions ->
      Box(
        modifier = Modifier
          .offset(y = 1.dp)
          .tabIndicatorOffset(tabPositions[selectedTabIndex])
          .height(4.dp)
          .background(color = MaterialTheme.colors.primary, shape = RoundedCornerShape(10.dp))

      )
    },
    divider = {
      Spacer(
        modifier = Modifier
          .padding(horizontal = 16.dp)
          .fillMaxWidth()
          .background(Color.DarkGray.copy(alpha = 0.1f))
          .height(1.dp)
      )
    },
    tabs = tabs,
  )
}

@Preview(showBackground = true)
@Composable
fun TabsPreview() {
  val titles = listOf("Tracks", "Artists", "Albums")
  TroonaScrollableTabRow(selectedTabIndex = 0) {
    titles.forEachIndexed { index, title ->
      TroonaTab(
        selected = index == 0,
        onClick = { },
        text = { Text(text = title) },
      )
    }
  }
}

object TroonaTabDefaults {
  val TabTopPadding = 0.dp
}
