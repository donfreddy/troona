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

package com.donfreddy.troona

import android.app.Application
import timber.log.Timber

class TroonaApplication : Application() {
  override fun onCreate() {
    super.onCreate()

    // Add Timber to the debug build only.
    Timber.plant(Timber.DebugTree())
  }
}