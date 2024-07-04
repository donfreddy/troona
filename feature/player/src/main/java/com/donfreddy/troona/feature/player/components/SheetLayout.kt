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

package com.donfreddy.troona.feature.player.components

import androidx.compose.runtime.Composable
import com.donfreddy.troona.feature.player.MoreBottomSheet
import com.donfreddy.troona.feature.player.QueueBottomSheet

/**
 * Enum class representing the type of content to be displayed in the bottom sheet.
 */
internal enum class BottomSheetType { MORE, QUEUE }

/**
 * Composable function to render a bottom sheet layout based on the specified [bottomSheetType].
 *
 * @param bottomSheetType The type of content to be displayed in the bottom sheet.
 * @param closeSheet Callback to close the bottom sheet.
 */
@Composable
internal fun SheetLayout(
  bottomSheetType: BottomSheetType,
  closeSheet: () -> Unit
) {
  // Based on the provided type, invoke the corresponding bottom sheet composable.
  when (bottomSheetType) {
    BottomSheetType.MORE -> MoreBottomSheet(closeSheet)
    BottomSheetType.QUEUE -> QueueBottomSheet(closeSheet)
  }
}