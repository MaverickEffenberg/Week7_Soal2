package com.maverick.week7soal2.ui.screens.homepage

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.maverick.week7soal2.model.Album
import com.maverick.week7soal2.model.Artist
import com.maverick.week7soal2.repository.ArtistRepository
import com.maverick.week7soal2.service.RetrofitClient
import kotlinx.coroutines.launch
import java.io.IOException

// State untuk Home/Artist Page
data class HomeUiState(
    val artist: Artist? = null,
    val albums: List<Album> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)

class HomeViewModel(private val repository: ArtistRepository) : ViewModel() {
    var uiState by mutableStateOf(HomeUiState())
        private set

    // Ganti "John Mayer" dengan artis favorit Anda
    private val artistName = "John Mayer"

    init {
        loadArtistData()
    }

    fun loadArtistData() {
        uiState = uiState.copy(isLoading = true, errorMessage = null)
        viewModelScope.launch {
            // Fetch Artist Detail
            val artistResult = repository.getArtistDetail(artistName)
            artistResult.onSuccess { artist ->
                // Fetch Albums if Artist Detail is successful
                val albumsResult = repository.getArtistAlbums(artistName)
                albumsResult.onSuccess { albums ->
                    uiState = HomeUiState(
                        artist = artist,
                        albums = albums,
                        isLoading = false,
                        errorMessage = null
                    )
                }.onFailure { e ->
                    handleError(e)
                }
            }.onFailure { e ->
                handleError(e)
            }
        }
    }

    private fun handleError(e: Throwable) {
        val message = when (e) {
            is IOException -> "Error: Tidak ada koneksi internet." // Sesuai gambar error
            is NoSuchElementException, is NullPointerException -> "Error: Data artis tidak ditemukan."
            else -> "Error: Gagal memuat data. ${e.localizedMessage ?: "Unknown error"}"
        }
        uiState = uiState.copy(
            isLoading = false,
            errorMessage = message
        )
    }

    // Factory untuk inisialisasi ViewModel
    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return HomeViewModel(
                    repository = ArtistRepository(RetrofitClient.apiService)
                ) as T
            }
        }
    }
}