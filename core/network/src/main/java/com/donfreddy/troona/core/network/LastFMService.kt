package com.donfreddy.troona.core.network

import com.donfreddy.troona.core.network.models.LastFmAlbum
import com.donfreddy.troona.core.network.models.LastFmArtist
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface LastFMService {
  @GET("$BASE_QUERY_PARAMS&method=artist.getinfo")
  suspend fun artistInfo(
    @Query(ARTIST) artistName: String,
    @Query(LANG) language: String?,
    @Header(CACHE_CONTROL) cacheControl: String?
  ): LastFmArtist

  @GET("$BASE_QUERY_PARAMS&method=album.getinfo")
  suspend fun albumInfo(
    @Query(ARTIST) artistName: String,
    @Query(LANG) language: String?,
    @Query(ALBUM) albumName: String
  ): LastFmAlbum

  companion object {
    private const val API_KEY = "c699484b20054763086fd0fb125036fa"
    private const val BASE_QUERY_PARAMS = "?format=json&autocorrect=1&api_key=$API_KEY"
    private const val ARTIST = "artist"
    private const val ALBUM = "album"
    private const val LANG = "lang"
    private const val CACHE_CONTROL = "Cache-Control"
  }
}