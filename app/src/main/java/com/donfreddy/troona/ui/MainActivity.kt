/*
 * Copyright 2024 Don Freddy
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.donfreddy.troona.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.media3.common.util.UnstableApi
import com.donfreddy.troona.core.designsystem.theme.TroonaTheme
import com.donfreddy.troona.core.model.enums.DarkThemeConfig
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
  private val viewModel: MainActivityViewModel by viewModels()


  @UnstableApi
  override fun onCreate(savedInstanceState: Bundle?) {
    handleIntent(intent)

    val splashScreen = installSplashScreen()

    super.onCreate(savedInstanceState)

    var uiState: MainActivityUiState by mutableStateOf(MainActivityUiState.Loading)

    // Update the uiState
    lifecycleScope.launch {
      lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
        viewModel.uiState.collectLatest { uiState = it }
      }
    }

    // Keep the splash screen on-screen until the UI state is loaded. This condition is
    // evaluated each time the app needs to be redrawn so it should be fast to avoid blocking
    // the UI.
    splashScreen.setKeepOnScreenCondition { uiState == MainActivityUiState.Loading }

    WindowCompat.setDecorFitsSystemWindows(window, false)

    @RequiresApi(Build.VERSION_CODES.Q)
    window.isNavigationBarContrastEnforced = false

    setContent {
      val isDarkTheme = shouldUseDarkTheme(uiState)
      val systemUiController = WindowInsetsControllerCompat(window, window.decorView)

      TroonaTheme(isDarkTheme = isDarkTheme) {
        TroonaApp(
          onSetSystemBarsLightIcons = {
            if (!isDarkTheme) {
              systemUiController.isAppearanceLightStatusBars = false
              systemUiController.isAppearanceLightNavigationBars = false
            }
          },
          onResetSystemBarsIcons = {
            if (!isDarkTheme) {
              systemUiController.isAppearanceLightStatusBars = true
              systemUiController.isAppearanceLightNavigationBars = true
            }
          },
        )
      }
    }
  }

  override fun onNewIntent(intent: Intent?) {
    super.onNewIntent(intent)
    handleIntent(intent)
  }

  private fun handleIntent(intent: Intent?) {
    intent?.let {
      val shortcutId = it.getStringExtra("shortcut_id")

      if (shortcutId != null) {
        Timber.tag("MainActivity").d("Shortcut clicked: $shortcutId")
      }
    }
  }
}

@Composable
private fun shouldUseDarkTheme(uiState: MainActivityUiState) = when (uiState) {
  MainActivityUiState.Loading -> isSystemInDarkTheme()
  is MainActivityUiState.Success -> when (uiState.userData.darkThemeConfig) {
    DarkThemeConfig.SystemDefault -> isSystemInDarkTheme()
    DarkThemeConfig.Light -> false
    DarkThemeConfig.Dark -> true
  }
}
