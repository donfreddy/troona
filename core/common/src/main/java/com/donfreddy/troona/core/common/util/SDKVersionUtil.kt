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

package com.donfreddy.troona.core.common.util

import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast

/**
 * Utility class for checking the sdk version of the device
 * running the application
 */
object SDKVersionUtil {
  /**
   * @return true if device is running on Android 6.0 (API 23) and higher
   */
  @get:ChecksSdkIntAtLeast(api = Build.VERSION_CODES.M)
  val isMarshmallowOrHigher: Boolean
    get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M

  /**
   * @return true if device is running on Android 7.0 (API 24) and higher
   */
  @get:ChecksSdkIntAtLeast(api = Build.VERSION_CODES.N)
  val isNougatOrHigher: Boolean
    get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N

  /**
   * @return true if device is running on Android 7.1 (API 25) and higher
   */
  @get:ChecksSdkIntAtLeast(api = Build.VERSION_CODES.N_MR1)
  val isNougatMR1OrHigher: Boolean
    get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1

  /**
   * @return true if device is running on Android 8.0 (API 26) and higher
   */
  @get:ChecksSdkIntAtLeast(api = Build.VERSION_CODES.O)
  val isOreoOrHigher: Boolean
    get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O

  /**
   * @return true if device is running on Android 8.1 (API 27) and higher
   */
  @get:ChecksSdkIntAtLeast(api = Build.VERSION_CODES.O_MR1)
  val isOreoMR1OrHigher: Boolean
    get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1

  /**
   * @return true if device is running on Android 9 (API 28) and higher
   */
  @get:ChecksSdkIntAtLeast(api = Build.VERSION_CODES.P)
  val isPieOrHigher: Boolean
    get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.P

  /**
   * @return true if device is running on Android 10 (API 29) and higher
   */
  @get:ChecksSdkIntAtLeast(api = Build.VERSION_CODES.Q)
  val isAndroid10OrHigher: Boolean
    get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

  /**
   * @return true if device is running on Android 11 (API 30) and higher
   */
  @get:ChecksSdkIntAtLeast(api = Build.VERSION_CODES.R)
  val isAndroid11OrHigher: Boolean
    get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.R

  /**
   * @return true if device is running on Android 12 (API 31) or later
   */
  @get:ChecksSdkIntAtLeast(api = Build.VERSION_CODES.S)
  val isAndroid12OrHigher: Boolean
    get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S

  /**
   * @return true if device is running on Android 13 (API 33) or later
   */
  @get:ChecksSdkIntAtLeast(api = Build.VERSION_CODES.TIRAMISU)
  val isTiramisuOrHigher: Boolean
    get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU

  /**
   * @return true if device is running on Android 14 (API 34) or later
   */
  @get:ChecksSdkIntAtLeast(api = Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
  val isUpsideDownCakeOrHigher: Boolean
    get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE
}