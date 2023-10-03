package com.example.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.google.gson.Gson
import android.content.Intent
import android.content.ServiceConnection
import android.icu.text.DateFormat
import android.icu.text.SimpleDateFormat
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class AudioPlayerActivity : AppCompatActivity() {

    private lateinit var selectableTrack: Track
    private lateinit var artWorkImage: ImageView
    private lateinit var nameOfTrack: TextView
    private lateinit var nameOfArtist: TextView
    private lateinit var buttonAddToLibrary: ImageView
    private lateinit var buttonPlay: ImageView
    private lateinit var buttonAddToFavorite: ImageView
    private lateinit var currentTrackTime: TextView
    private lateinit var trackTime: TextView
    private lateinit var nameOfArtWork: TextView
    private lateinit var yearOfTrack: TextView
    private lateinit var genreOfTrack: TextView
    private lateinit var countryOfTrack: TextView
    private lateinit var backButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)
        initializeComponents()
        setInActivityElementsValueOfTrack()
    }

    fun initializeComponents() {
        artWorkImage = findViewById(R.id.cover_art_work_image)
        nameOfTrack = findViewById(R.id.name_of_track_audio_player_activity)
        nameOfArtist = findViewById(R.id.name_of_artist_audio_player_actovity)
        buttonAddToLibrary = findViewById(R.id.addToLibraryButton)
        buttonPlay = findViewById(R.id.playButton)
        buttonAddToFavorite = findViewById(R.id.favoriteButton)
        currentTrackTime = findViewById(R.id.current_track_time_audio_player)
        trackTime = findViewById(R.id.track_time_value_audio_player)
        nameOfArtWork = findViewById(R.id.artwork_value_audio_player)
        yearOfTrack = findViewById(R.id.year_value_audio_player)
        genreOfTrack = findViewById(R.id.genre_value_audio_player)
        countryOfTrack = findViewById(R.id.country_value_audio_player)
        backButton = findViewById(R.id.backButton)
        selectableTrack = getTrack(intent.getStringExtra(SELECTABLE_TRACK))
        backButton.setOnClickListener {
            finish()
        }
    }

    fun setInActivityElementsValueOfTrack() {
        nameOfTrack.text = selectableTrack.trackName
        nameOfArtist.text = selectableTrack.artistName
        trackTime.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(selectableTrack.trackTimeMillis)

        yearOfTrack.text = LocalDateTime.parse(
            selectableTrack.releaseDate,
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
        ).year.toString()

        Glide.with(artWorkImage)
            .load(selectableTrack.getCoverArtWork())
            .placeholder(R.drawable.placeholder)
            .centerCrop()
            .transform(RoundedCorners(artWorkImage.resources.getDimensionPixelSize(R.dimen.image_artwork_corner_radius_audio_player)))
            .into(artWorkImage)
        genreOfTrack.text = selectableTrack.primaryGenreName
        nameOfArtWork.text = selectableTrack.collectionName
        countryOfTrack.text = selectableTrack.country
        currentTrackTime.text = SimpleDateFormat(
            "mm:ss",
            Locale.getDefault()
        ).format(selectableTrack.trackTimeMillis) //пока так
    }

    fun getTrack(key: String?) = Gson().fromJson(key, Track::class.java)

    companion object {
        const val SELECTABLE_TRACK = "selectable_track"
    }


}