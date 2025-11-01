package com.maverick.week7soal2.model

import com.google.gson.annotations.SerializedName

data class AlbumResponse(
    @SerializedName("album")
    val albums: List<Album>?
)

data class Album(
    @SerializedName("idAlbum")
    val idAlbum: String,
    @SerializedName("idArtist")
    val idArtist: String,
    @SerializedName("strAlbum")
    val strAlbum: String,
    @SerializedName("intYearReleased")
    val intYearReleased: String?,
    @SerializedName("strGenre")
    val strGenre: String?,
    @SerializedName("strAlbumThumb")
    val strAlbumThumb: String?,
    @SerializedName("strDescriptionEN")
    val strDescriptionEN: String?,
    @SerializedName("strArtist")
    val strArtist: String? // Tambahan untuk detail album
)

// Model untuk Detail Album
data class AlbumDetailResponse(
    @SerializedName("album")
    val albums: List<Album>?
)