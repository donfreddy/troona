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

package com.donfreddy.troona.core.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.donfreddy.troona.core.designsystem.icon.TroonaIcons
import com.donfreddy.troona.core.designsystem.theme.spacing
import com.donfreddy.troona.core.model.enums.AlbumSortBy
import com.donfreddy.troona.core.model.enums.ArtistSortBy
import com.donfreddy.troona.core.model.enums.SongSortBy
import com.donfreddy.troona.core.model.enums.SortOrder

data class DropDownItem(
  val text: String,
  val selected: Boolean,
  // val onItemSelected: (Int) -> Unit
)

@Composable
internal fun MediaHeader(
  sortParams: SortParams,
  itemCountString: String,
  modifier: Modifier = Modifier,
  dropdownItems: List<DropDownItem>,
) {
  var expanded by remember { mutableStateOf(false) }

  Column(
    modifier = modifier
      .fillMaxWidth()
     .padding(MaterialTheme.spacing.medium)
  ) {
    Row(
      modifier = modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = itemCountString,
        style = MaterialTheme.typography.bodySmall.copy(
          fontWeight = FontWeight.Bold,
        ),
      )

      Box {
        TextButton(
          modifier = Modifier.height(24.dp),
          onClick = { expanded = !expanded },
          contentPadding = PaddingValues(MaterialTheme.spacing.default),
        ) {
          Row(
            modifier = modifier.padding(horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "Date Added", style = MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colorScheme.primary
              )
            )
            Spacer(modifier = modifier.width(5.dp))
            Icon(
              painter = painterResource(id = TroonaIcons.Sort.resourceId),
              modifier = Modifier.size(16.dp),
              contentDescription = "Sort"
            )
          }
        }

        DropdownMenu(
          expanded = expanded,
          onDismissRequest = { expanded = false },
          offset = DpOffset((-8).dp, 4.dp)
        ) {
          dropdownItems.forEachIndexed { index, item ->
            DropdownMenuItem(
              text = {
                Column {
                  Row(
                    modifier = modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                  ) {
                    Text(
                      text = item.text,
                      style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    RadioButton(selected = item.selected, colors = RadioButtonDefaults.colors(
                      selectedColor = MaterialTheme.colorScheme.primary,
                      unselectedColor = MaterialTheme.colorScheme.primary
                    ), onClick = {
                      expanded = false
                      // onItemClick(item)
                    })
                  }
                  if (index < dropdownItems.size - 1) TroonaDivider()
                }
              },
              onClick = {
              expanded = false
              // onItemClick(item)
            })
          }
        }
      }
    }
  }
}

data class SortParams(
  val sortOrder: SortOrder,
  val songSortBy: SongSortBy,
  val artistSortBy: ArtistSortBy,
  val albumSortBy: AlbumSortBy,
  val onChangeSortOrder: (SortOrder) -> Unit,
  val onChangeSongSortBy: (SongSortBy) -> Unit,
  val onChangeArtistSortBy: (ArtistSortBy) -> Unit,
  val onChangeAlbumSortBy: (AlbumSortBy) -> Unit,
)

@Preview
@Composable
fun MediaHeaderPreview() {
  MediaHeader(dropdownItems = listOf(
    DropDownItem("Date Added", true),
    DropDownItem("Date Modified", false)
  ),
    sortParams = SortParams(
      sortOrder = SortOrder.Ascending,
      songSortBy = SongSortBy.Title,
      artistSortBy = ArtistSortBy.NumberOfSongs,
      albumSortBy = AlbumSortBy.Artist,
      onChangeSortOrder = {},
      onChangeSongSortBy = {},
      onChangeArtistSortBy = {},
      onChangeAlbumSortBy = {}
    ),
    itemCountString = "360 songs"
  )
}