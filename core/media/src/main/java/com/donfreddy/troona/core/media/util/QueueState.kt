package com.donfreddy.troona.core.media.util

import androidx.media3.common.MediaItem

interface QueueState {
  val queue: List<MediaItem>?
}