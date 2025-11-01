package com.maverick.week7soal2.model

import com.google.gson.annotations.SerializedName

data class ArtistResponse(
    @SerializedName("artists")
    val artists: List<Artist>?
)

data class Artist(
    @SerializedName("idArtist")
    val idArtist: String,
    @SerializedName("strArtist")
    val strArtist: String,
    @SerializedName("strGenre")
    val strGenre: String?,
    @SerializedName("strBiographyEN")
    val strBiographyEN: String?,
    @SerializedName("strArtistBanner")
    val strArtistBanner: String?, // Banner (optional)
    @SerializedName("strArtistThumb")
    val strArtistThumb: String? // Thumb for smaller display
)