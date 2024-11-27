package com.donfreddy.troona.feature.player

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.donfreddy.troona.core.designsystem.theme.dimens

@Composable
fun MoreBottomSheet(closeSheet: () -> Unit) {
  Column() {
    repeat(30) { index ->
      Row(horizontalArrangement = Arrangement.spacedBy(20.dp),
        modifier = Modifier
          .clickable { /* TODO */ }
          .fillMaxWidth()
          .padding(vertical = 10.dp)) {
        Icon(
          Icons.Rounded.ShoppingCart, contentDescription = null
        )
        Text("More $index")
      }
    }
  }
}