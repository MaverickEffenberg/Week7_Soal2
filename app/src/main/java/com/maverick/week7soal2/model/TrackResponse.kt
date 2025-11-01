package com.maverick.week7soal2.model

import com.google.gson.annotations.SerializedName

data class TrackResponse(
    @SerializedName("track")
    val tracks: List<Track>?
)

data class Track(
    @SerializedName("idTrack")
    val idTrack: String,
    @SerializedName("idAlbum")
    val idAlbum: String,
    @SerializedName("idArtist")
    val idArtist: String,
    @SerializedName("strTrack")
    val strTrack: String,
    @SerializedName("intTrackNumber")
    val intTrackNumber: String?,
    @SerializedName("intDuration") // Durasi dalam milidetik
    val intDuration: String?
)