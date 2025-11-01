package com.maverick.week7soal2.repository

import com.maverick.week7soal2.model.Album
import com.maverick.week7soal2.model.Artist
import com.maverick.week7soal2.model.Track
import com.maverick.week7soal2.service.ApiService
import java.io.IOException

class ArtistRepository(private val apiService: ApiService) {

    // Mengambil detail artis
    suspend fun getArtistDetail(artistName: String): Result<Artist> = try {
        val response = apiService.searchArtist(artistName)
        val artist = response.artists?.firstOrNull()
        if (artist != null) {
            Result.success(artist)
        } else {
            Result.failure(Exception("Artist not found"))
        }
    } catch (e: IOException) {
        Result.failure(e) // Network or IO Error
    } catch (e: Exception) {
        Result.failure(e) // Other errors
    }

    // Mengambil daftar album
    suspend fun getArtistAlbums(artistName: String): Result<List<Album>> = try {
        val response = apiService.searchArtistAlbums(artistName)
        val albums = response.albums.orEmpty()
            // Filter album yang tidak memiliki cover, agar UI terlihat baik
            .filter { !it.strAlbumThumb.isNullOrBlank() }
        Result.success(albums)
    } catch (e: IOException) {
        Result.failure(e)
    } catch (e: Exception) {
        Result.failure(e)
    }

    // Mengambil detail album
    suspend fun getAlbumDetail(albumId: String): Result<Album> = try {
        val response = apiService.getAlbumDetail(albumId)
        val album = response.albums?.firstOrNull()
        if (album != null) {
            Result.success(album)
        } else {
            Result.failure(Exception("Album detail not found"))
        }
    } catch (e: IOException) {
        Result.failure(e)
    } catch (e: Exception) {
        Result.failure(e)
    }

    // Mengambil daftar track album
    suspend fun getAlbumTracks(albumId: String): Result<List<Track>> = try {
        val response = apiService.getAlbumTracks(albumId)
        Result.success(response.tracks.orEmpty())
    } catch (e: IOException) {
        Result.failure(e)
    } catch (e: Exception) {
        Result.failure(e)
    }
}