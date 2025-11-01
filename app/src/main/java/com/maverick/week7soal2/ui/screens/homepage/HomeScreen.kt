package com.maverick.week7soal2.ui.screens.homepage

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
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
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.maverick.week7soal2.model.Album
import com.maverick.week7soal2.model.Artist
import com.maverick.week7soal2.ui.theme.OrangeRetro
import com.maverick.week7soal2.ui.theme.PrimaryDark
import com.maverick.week7soal2.ui.theme.SecondaryDark
import com.maverick.week7soal2.ui.theme.TransparentBlack

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(factory = HomeViewModel.Factory),
    onAlbumClick: (String) -> Unit
) {
    val state = viewModel.uiState

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(state.artist?.strArtist ?: "Artist Explorer") },
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
                onRetry = viewModel::loadArtistData
            )
            state.artist != null -> ArtistContent(
                artist = state.artist,
                albums = state.albums,
                modifier = Modifier.padding(padding),
                onAlbumClick = onAlbumClick
            )
        }
    }
}

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Loading...", color = Color.White)
        Spacer(Modifier.height(16.dp))
        CircularProgressIndicator(color = OrangeRetro)
    }
}

@Composable
fun ErrorScreen(message: String, modifier: Modifier = Modifier, onRetry: () -> Unit) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SecondaryDark),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Warning,
            contentDescription = "Error",
            tint = OrangeRetro,
            modifier = Modifier.size(64.dp)
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = "Error",
            color = Color.Red,
            fontSize = MaterialTheme.typography.headlineSmall.fontSize
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = message,
            color = OrangeRetro,
            fontSize = MaterialTheme.typography.bodyLarge.fontSize
        )
        Spacer(Modifier.height(16.dp))
        Button(onClick = onRetry, colors = ButtonDefaults.buttonColors(containerColor = OrangeRetro)) {
            Text("Coba Lagi")
        }
    }
}

@Composable
fun ArtistContent(artist: Artist, albums: List<Album>, modifier: Modifier, onAlbumClick: (String) -> Unit) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        item {
            ArtistHeader(artist)
        }
        item {
            Text(
                "Albums",
                style = MaterialTheme.typography.headlineSmall,
                color = Color.White,
                modifier = Modifier.padding(16.dp, 8.dp)
            )
        }
        item {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .heightIn(max = 1000.dp)
                    .fillMaxWidth()
            ) {
                items(albums) { album ->
                    AlbumGridItem(album = album, onAlbumClick = onAlbumClick)
                }
            }
        }
    }
}

@Composable
fun ArtistHeader(artist: Artist) {
    val imagePainter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(artist.strArtistBanner ?: artist.strArtistThumb)
            .crossfade(true)
            .build()
    )
    val imageState = imagePainter.state

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
    ) {
        // Background Image/Banner
        Image(
            painter = imagePainter,
            contentDescription = artist.strArtist,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Overlay untuk teks
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(TransparentBlack)
                .padding(16.dp),
            verticalArrangement = Arrangement.Bottom
        ) {
            Text(
                text = artist.strArtist,
                style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
                color = Color.White
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = artist.strGenre ?: "Genre Unknown",
                style = MaterialTheme.typography.bodyLarge,
                color = OrangeRetro
            )
        }

        // Sesuai gambar: John Mayer, Album List
        if (imageState is AsyncImagePainter.State.Error) {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = "Image Error",
                tint = Color.Gray,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }

    // Biografi Singkat
    Column(modifier = Modifier.padding(16.dp)) {
        val bio = artist.strBiographyEN?.take(300) ?: "No biography available."
        Text(
            text = bio + if (artist.strBiographyEN?.length ?: 0 > 300) "..." else "",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.LightGray
        )
    }
}


@Composable
fun AlbumGridItem(album: Album, onAlbumClick: (String) -> Unit) {
    OutlinedCard(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.outlinedCardColors(containerColor = PrimaryDark),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onAlbumClick(album.idAlbum) }
    ) {
        Column {
            Image(
                painter = rememberAsyncImagePainter(album.strAlbumThumb),
                contentDescription = album.strAlbum,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
            )
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = album.strAlbum,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "${album.intYearReleased ?: "N/A"} • ${album.strGenre ?: "Indie"}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.LightGray
                )
            }
        }
    }
}