package com.example.playlistmaker

import android.os.Build
import android.os.Build.VERSION
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.databinding.ActivityAudioPlayerBinding

class AudioPlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAudioPlayerBinding
    private lateinit var selectableTrack: Track


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initializeComponents()
        setInActivityElementsValueOfTrack()
    }

    private fun initializeComponents() {
        if (VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            selectableTrack = intent.getParcelableExtra(TrackAdapter.SELECTABLE_TRACK, Track::class.java)!!
        } else {
            selectableTrack = intent.getParcelableExtra(TrackAdapter.SELECTABLE_TRACK)!!
        }
        binding.backButton.setOnClickListener {
            finish()
        }
    }

    private fun setInActivityElementsValueOfTrack() {
        binding.nameOfTrackAudioPlayerActivity.isSelected = true
        binding.nameOfTrackAudioPlayerActivity.text = selectableTrack.trackName
        binding.nameOfArtistAudioPlayerActovity.text = selectableTrack.artistName
        binding.trackTimeValueAudioPlayer.text =
            selectableTrack.getSimpleDateFormat(selectableTrack)
        binding.yearValueAudioPlayer.text = selectableTrack.getLocalDateTime(selectableTrack)

        Glide.with(binding.coverArtWorkImage)
            .load(selectableTrack.getCoverArtWork())
            .placeholder(R.drawable.placeholder)
            .centerCrop()
            .transform(RoundedCorners(binding.coverArtWorkImage.resources.getDimensionPixelSize(R.dimen.image_artwork_corner_radius_audio_player)))
            .into(binding.coverArtWorkImage)
        binding.genreValueAudioPlayer.text = selectableTrack.primaryGenreName
        binding.artworkValueAudioPlayer.text = if (selectableTrack.collectionName!!.isNotEmpty()) selectableTrack.collectionName else "Нет данных"
        binding.countryValueAudioPlayer.text = selectableTrack.country
        binding.currentTrackTimeAudioPlayer.text =
            selectableTrack.getSimpleDateFormat(selectableTrack)
    }

}