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

package com.donfreddy.troona.core.database.dao

import androidx.room.Dao

@Dao
interface PlaylistDao {

/*  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun createPlaylist(playlistEntity: PlaylistEntity): Long

  @Query("SELECT * FROM playlists WHERE name = :name")
  fun getPlaylist(name: String): List<PlaylistEntity>

  @Query("SELECT * FROM playlists")
  suspend fun getPlaylists(): List<PlaylistEntity>

  @Query("UPDATE playlists SET name = :name WHERE id = :playlistId")
  suspend fun renamePlaylist(playlistId: Long, name: String)

  @Query("DELETE FROM songs WHERE playlist_id = :playlistId")
  suspend fun deletePlaylistSongs(playlistId: Long)

  @Query("DELETE FROM songs WHERE playlist_id = :playlistId AND id = :songId")
  suspend fun deleteSongFromPlaylist(playlistId: Long, songId: Long)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertSongsToPlaylist(songEntities: List<SongEntity>)

  @Query("SELECT * FROM songs WHERE playlist_id = :playlistId AND id = :songId")
  suspend fun isSongExistsInPlaylist(playlistId: Long, songId: Long): List<SongEntity>

  @Query("SELECT * FROM songs WHERE playlist_id = :playlistId ORDER BY song_key asc")
  fun songsFromPlaylist(playlistId: Long): LiveData<List<SongEntity>>

  @Delete
  suspend fun deletePlaylist(playlistEntity: PlaylistEntity)

  @Delete
  suspend fun deletePlaylists(playlistEntities: List<PlaylistEntity>)

  @Delete
  suspend fun deletePlaylistSongs(songs: List<SongEntity>)*/
}

/// @See
/// https://github.com/RetroMusicPlayer/RetroMusicPlayer/blob/dev/app/src/main/java/code/name/monkey/retromusic/db/PlaylistDao.kt