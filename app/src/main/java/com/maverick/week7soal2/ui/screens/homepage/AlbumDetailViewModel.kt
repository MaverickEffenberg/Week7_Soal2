package com.maverick.week7soal2.ui.screens.homepage

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.maverick.week7soal2.model.Album
import com.maverick.week7soal2.model.Track
import com.maverick.week7soal2.repository.ArtistRepository
import com.maverick.week7soal2.service.RetrofitClient
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.io.IOException

// State untuk Album Detail Page
data class AlbumDetailUiState(
    val album: Album? = null,
    val tracks: List<Track> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)

class AlbumDetailViewModel(private val repository: ArtistRepository, private val albumId: String) : ViewModel() {
    var uiState by mutableStateOf(AlbumDetailUiState())
        private set

    init {
        if (albumId.isNotBlank()) {
            loadAlbumDetail(albumId)
        } else {
            uiState = uiState.copy(isLoading = false, errorMessage = "Error: Album ID tidak valid.")
        }
    }

    private fun loadAlbumDetail(id: String) {
        uiState = uiState.copy(isLoading = true, errorMessage = null)
        viewModelScope.launch {
            try {
                // Fetch Detail dan Tracks secara paralel
                val detailDeferred = async { repository.getAlbumDetail(id) }
                val tracksDeferred = async { repository.getAlbumTracks(id) }

                val detailResult = detailDeferred.await()
                val tracksResult = tracksDeferred.await()

                detailResult.onSuccess { album ->
                    tracksResult.onSuccess { tracks ->
                        uiState = AlbumDetailUiState(
                            album = album,
                            tracks = tracks.sortedBy { it.intTrackNumber?.toIntOrNull() ?: Int.MAX_VALUE },
                            isLoading = false
                        )
                    }
                }.onFailure { e ->
                    handleError(e)
                }
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }

    private fun handleError(e: Throwable) {
        val message = when (e) {
            is IOException -> "Error: Tidak ada koneksi internet."
            is NoSuchElementException, is NullPointerException -> "Error: Detail album tidak ditemukan."
            else -> "Error: Gagal memuat data. ${e.localizedMessage ?: "Unknown error"}"
        }
        uiState = uiState.copy(
            isLoading = false,
            errorMessage = message
        )
    }

    // Factory untuk inisialisasi ViewModel dengan argumen (albumId)
    companion object {
        fun Factory(albumId: String): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return AlbumDetailViewModel(
                    repository = ArtistRepository(RetrofitClient.apiService),
                    albumId = albumId
                ) as T
            }
        }
    }
}