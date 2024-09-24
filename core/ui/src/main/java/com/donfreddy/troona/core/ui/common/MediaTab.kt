package com.donfreddy.troona.core.ui.common

import androidx.annotation.StringRes
import com.donfreddy.troona.core.ui.R

internal enum class MediaTab(@StringRes val titleRes: Int) {
  // SUGGESTED(R.string.core_ui_suggested_title),
  SONGS(R.string.core_ui_songs_title),
  ARTISTS(R.string.core_ui_artists_title),
  ALBUMS(R.string.core_ui_albums_title),
}