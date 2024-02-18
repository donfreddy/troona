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

package com.donfreddy.troona.core.permission

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.donfreddy.troona.core.designsystem.theme.TroonaTheme
import com.donfreddy.troona.core.designsystem.theme.spacing
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionContent(
  permissionState: PermissionState,
  isPermissionRequested: Boolean,
  modifier: Modifier = Modifier,
  context: Context = LocalContext.current
) {
  val isPermissionDenied = isPermissionRequested && !permissionState.status.shouldShowRationale

  Scaffold(modifier = modifier.systemBarsPadding(), topBar = {
    TopAppBar(
      title = {
        Text(
          text = stringResource(id = R.string.core_permission_app_name),
          style = MaterialTheme.typography.h5,
          color = MaterialTheme.colors.onPrimary,
          fontWeight = FontWeight.SemiBold,
          textAlign = TextAlign.Center
        )
      },
      backgroundColor = MaterialTheme.colors.primary,
    )
  }) { innerPadding ->
    Column(
      modifier = Modifier
        .padding(innerPadding)
        .consumeWindowInsets(innerPadding)
        .padding(MaterialTheme.spacing.extraMedium)
        .fillMaxSize(),
      verticalArrangement = Arrangement.Center
    ) {
      Text(
        text = stringResource(id = R.string.core_permission_storage_access),
        style = MaterialTheme.typography.h5,
      )

      Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

      Text(
        text = stringResource(id = if (isPermissionDenied) R.string.core_permission_storage_settings_permission_text else R.string.core_permission_storage_access_permission_text),
        style = MaterialTheme.typography.body2,
      )

      Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

      if (isPermissionDenied) {
        Button(
          modifier = Modifier.fillMaxWidth(),
          onClick = context::openSettings,
          shape = MaterialTheme.shapes.medium
        ) {
          Text(text = stringResource(id = R.string.core_permission_storage_open_settings))
        }
      } else {
        Button(
          modifier = Modifier.fillMaxWidth(),
          onClick = permissionState::launchPermissionRequest,
          shape = MaterialTheme.shapes.medium
        ) {
          Text(text = stringResource(id = R.string.core_permission_grant_access))
        }
      }
    }
  }
}

private fun Context.openSettings() = Intent(
  Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", packageName, null)
).let(::startActivity)


@OptIn(ExperimentalPermissionsApi::class)
@Preview(showBackground = true)
@Composable
fun PermissionContentPreview() {
  TroonaTheme {
    val storagePermissionState =
      rememberPermissionState(android.Manifest.permission.READ_EXTERNAL_STORAGE)
    PermissionContent(
      permissionState = storagePermissionState, isPermissionRequested = true
    )
  }
}
