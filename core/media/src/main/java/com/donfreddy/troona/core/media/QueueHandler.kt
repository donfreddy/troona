package com.donfreddy.troona.core.media

import com.donfreddy.troona.core.model.data.Song

/**
 * [QueueHandler] is a class that manages the queue of media items.
 * It provides all for updating and navigating the queue.
 */
class QueueHandler {
  private val queue = mutableListOf<Song>()
  private var currentIndex = -1
  private val listener: OnQueueChangeListener? = null

  fun addQueueItem(song: Song) {
    queue.add(song)
    if (this.listener != null) {
      listener.onQueueUpdated(queue);
    }
  }

  fun addQueueItems(songs: List<Song>) {
    queue.addAll(songs)
    if (this.listener != null) {
      listener.onQueueUpdated(queue);
    }
  }

  fun insertQueueItem(index: Int, song: Song) {
    queue.add(index, song)
    if (this.listener != null) {
      listener.onQueueUpdated(queue);
    }
  }

  fun setCurrentIndex(index: Int) {
    currentIndex = index
    if (this.listener != null) {
      listener.onCurrentIndexChanged(currentIndex);
    }
  }

  fun removeQueueItem(song: Song) {
    queue.remove(song)
    if (currentIndex >= queue.size) {
      currentIndex = queue.size - 1
    }
    if (this.listener != null) {
      listener.onQueueUpdated(queue);
    }
  }

  fun removeQueueItem(index: Int) {
    queue.removeAt(index)
    if (this.listener != null) {
      listener.onQueueUpdated(queue);
    }
  }

  fun getQueue() = queue
  fun getQueueSize() = queue.size
  fun getCurrentIndex() = currentIndex

  fun clearQueue() {
    queue.clear()
    currentIndex = -1
    if (this.listener != null) {
      listener.onQueueUpdated(queue);
    }
  }
}

interface OnQueueChangeListener {
  fun onQueueUpdated(queue: List<Song?>?)
  fun onCurrentIndexChanged(currentIndex: Int)
}