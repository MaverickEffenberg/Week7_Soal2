package com.maverick.week7soal2.ui.screens.homepage

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.maverick.week7soal2.model.Album
import com.maverick.week7soal2.model.Track
import com.maverick.week7soal2.ui.theme.OrangeRetro
import com.maverick.week7soal2.ui.theme.PrimaryDark
import com.maverick.week7soal2.ui.theme.SecondaryDark
import com.maverick.week7soal2.ui.theme.TransparentBlack
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumDetailScreen(
    albumId: String,
    onBackClick: () -> Unit
) {
    val viewModel: AlbumDetailViewModel = viewModel(factory = AlbumDetailViewModel.Factory(albumId))
    val state = viewModel.uiState

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(state.album?.strAlbum ?: "Detail Album") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = OrangeRetro
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PrimaryDark)
            )
        },
        containerColor = SecondaryDark
    ) { padding ->
        when {
            state.isLoading -> LoadingScreen(modifier = Modifier.padding(padding))
            state.errorMessage != null -> ErrorScreen(
                message = state.errorMessage,
                modifier = Modifier.padding(padding),
                onRetry = { /* Tidak ada retry di detail screen untuk sederhana */ }
            )
            state.album != null -> AlbumDetailContent(
                album = state.album,
                tracks = state.tracks,
                modifier = Modifier.padding(padding)
            )
        }
    }
}

@Composable
fun AlbumDetailContent(album: Album, tracks: List<Track>, modifier: Modifier) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        item {
            AlbumHeader(album)
        }
        item {
            Text(
                "Tracks",
                style = MaterialTheme.typography.headlineSmall,
                color = Color.White,
                modifier = Modifier.padding(16.dp, 8.dp)
            )
        }
        itemsIndexed(tracks) { index, track ->
            TrackListItem(track = track, index = index + 1)
        }
        item {
            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
fun AlbumHeader(album: Album) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedCard(
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.outlinedCardColors(containerColor = PrimaryDark),
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(album.strAlbumThumb)
                        .crossfade(true)
                        .build()
                ),
                contentDescription = album.strAlbum,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(300.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
        }

        Spacer(Modifier.height(16.dp))

        // Informasi Album
        Text(
            text = album.strAlbum,
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            color = Color.White
        )
        Text(
            text = "${album.intYearReleased ?: "N/A"} • ${album.strGenre ?: "Indie"}",
            style = MaterialTheme.typography.bodyLarge,
            color = OrangeRetro
        )

        Spacer(Modifier.height(8.dp))

        // Deskripsi Album
        val desc = album.strDescriptionEN?.take(400) ?: "No description available."
        Text(
            text = desc + if (album.strDescriptionEN?.length ?: 0 > 400) "..." else "",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.LightGray
        )
    }
}

@Composable
fun TrackListItem(track: Track, index: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Nomor Track
        Text(
            text = track.intTrackNumber ?: index.toString(),
            style = MaterialTheme.typography.titleMedium,
            color = OrangeRetro,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.width(30.dp)
        )
        Spacer(Modifier.width(16.dp))

        // Nama Track
        Text(
            text = track.strTrack,
            style = MaterialTheme.typography.titleMedium,
            color = Color.White,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )
        Spacer(Modifier.width(16.dp))

        // Durasi
        val duration = track.intDuration?.toLongOrNull()
        val formattedDuration = if (duration != null) {
            val minutes = TimeUnit.MILLISECONDS.toMinutes(duration)
            val seconds = TimeUnit.MILLISECONDS.toSeconds(duration) % 60
            String.format("%d:%02d", minutes, seconds)
        } else {
            "N/A"
        }
        Text(
            text = formattedDuration,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )
    }
}