package com.example.playlistmaker.presentation.ui.audioplayer

import android.os.Build
import android.os.Build.VERSION
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.Creator
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.presentation.TrackAdapter
import com.example.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.example.playlistmaker.domain.api.media_player.PlayerInteractor
import com.example.playlistmaker.domain.models.PlayerParams
import com.example.playlistmaker.domain.models.PlayerState
import com.example.playlistmaker.presentation.mapper.TrackMapper
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAudioPlayerBinding
    private lateinit var selectableTrack: Track
    private var mainTreadHandler: Handler? = null
    private lateinit var playerInterator: PlayerInteractor
    private lateinit var playerParams: PlayerParams


    override fun onPause() {
        super.onPause()
        playerInterator.pausePreviewTrack()
        mainTreadHandler?.post(changeProgressAndImage())
        changeProgressAndImage()
    }

    override fun onDestroy() {
        super.onDestroy()
        mainTreadHandler?.removeCallbacks(changeProgressAndImage())
        playerInterator.destroyPlayer()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        selectableTrack = if (VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(TrackAdapter.SELECTABLE_TRACK, Track::class.java)!!
        } else {
            intent.getParcelableExtra(TrackAdapter.SELECTABLE_TRACK)!!
        }
        playerInterator = Creator.providePlayerInteractor()
        initializeComponents()
        setInActivityElementsValueOfTrack()
    }

    private fun initializeComponents() {
        mainTreadHandler = Handler(Looper.getMainLooper())

        binding.backButton.setOnClickListener {
            finish()
        }

        binding.playButton.setOnClickListener {
            if (selectableTrack.previewUrl.equals(getString(R.string.no_data))) {
                Toast.makeText(this, getString(R.string.can_not_play), Toast.LENGTH_LONG).show()
            } else {
                playerParams = playerInterator.changePlaybackProgress()
                when (playerParams.playerState) {
                    PlayerState.PLAYING -> {
                        playerInterator.pausePreviewTrack()
                        mainTreadHandler?.post(changeProgressAndImage())
                    }

                    PlayerState.PAUSE, PlayerState.PREPARED -> {
                        playerInterator.startPreviewTrack()
                        mainTreadHandler?.post(changeProgressAndImage())
                    }

                    PlayerState.DEFAULT -> {
                        playerInterator.prepareMediaPlayer(selectableTrack)
                        playerInterator.startPreviewTrack()
                        mainTreadHandler?.post(changeProgressAndImage())
                    }
                }
            }
        }

    }

    private fun changeProgressAndImage(): Runnable {
        return object : Runnable {
            override fun run() {
                playerParams = playerInterator.changePlaybackProgress()
                when (playerParams.playerState) {
                    PlayerState.PLAYING -> {
                        binding.currentTrackTimeAudioPlayer.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(playerParams.currentPosition)
                        binding.playButton.setImageResource(R.drawable.button_pause)
                        mainTreadHandler?.postDelayed(this, DELAY_CURRENT_TIME_1000_MILLIS)
                    }
                    PlayerState.PAUSE -> {
                        binding.playButton.setImageResource(R.drawable.button_play)
                        mainTreadHandler?.removeCallbacks(this)
                    }
                    PlayerState.PREPARED -> {
                        binding.playButton.setImageResource(R.drawable.button_play)
                        binding.currentTrackTimeAudioPlayer.text =
                            getString(R.string.zero_playback_progress)
                    }
                    PlayerState.DEFAULT -> {
                        binding.playButton.setImageResource(R.drawable.button_play)
                    }
                }
            }
        }
    }

    private fun setInActivityElementsValueOfTrack() {
        binding.nameOfTrackAudioPlayerActivity.isSelected = true
        binding.nameOfTrackAudioPlayerActivity.text = selectableTrack.trackName
        binding.nameOfArtistAudioPlayerActovity.text = selectableTrack.artistName
        binding.trackTimeValueAudioPlayer.text = TrackMapper.getSimpleDateFormat(selectableTrack)

        binding.yearValueAudioPlayer.text = if (selectableTrack.releaseDate.equals(getString(R.string.no_data))) {
            getString(R.string.no_data)
        } else {
            TrackMapper.getLocalDateTime(selectableTrack)
        }
        Glide.with(binding.coverArtWorkImage)
            .load(TrackMapper.getCoverArtWork(selectableTrack))
            .placeholder(R.drawable.placeholder)
            .centerCrop()
            .transform(RoundedCorners(binding.coverArtWorkImage.resources.getDimensionPixelSize(R.dimen.image_artwork_corner_radius_audio_player)))
            .into(binding.coverArtWorkImage)
        binding.genreValueAudioPlayer.text = selectableTrack.primaryGenreName
        binding.artworkValueAudioPlayer.text = if (selectableTrack.collectionName!!.isNotEmpty()) selectableTrack.collectionName else "Нет данных"
        binding.countryValueAudioPlayer.text = selectableTrack.country
    }


    companion object {
        private const val DELAY_CURRENT_TIME_1000_MILLIS = 1000L
    }

}