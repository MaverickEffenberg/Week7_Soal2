package com.maverick.week7soal2.service

import com.maverick.week7soal2.model.AlbumDetailResponse
import com.maverick.week7soal2.model.AlbumResponse
import com.maverick.week7soal2.model.ArtistResponse
import com.maverick.week7soal2.model.TrackResponse
import retrofit2.http.GET
import retrofit2.http.Query

// Ganti "123" dengan API Key TheAudioDB yang berlaku,
// meskipun "123" adalah test key yang sering berfungsi untuk beberapa endpoint.
const val API_KEY = "123"
const val BASE_URL = "https://www.theaudiodb.com/api/v1/json/$API_KEY/"

interface ApiService {

    // Cari artis: search.php?s={artistName}
    @GET("search.php")
    suspend fun searchArtist(@Query("s") artistName: String): ArtistResponse

    // Cari album artis: searchalbum.php?s={artistName}
    @GET("searchalbum.php")
    suspend fun searchArtistAlbums(@Query("s") artistName: String): AlbumResponse

    // Detail album: album.php?m={albumId}
    @GET("album.php")
    suspend fun getAlbumDetail(@Query("m") albumId: String): AlbumDetailResponse

    // Track album: track.php?m={albumId}
    @GET("track.php")
    suspend fun getAlbumTracks(@Query("m") albumId: String): TrackResponse
}