package com.example.playlistmaker.ui.audioplayer

import android.media.MediaPlayer
import android.os.Build
import android.os.Build.VERSION
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.presentation.TrackAdapter
import com.example.playlistmaker.databinding.ActivityAudioPlayerBinding
import java.text.SimpleDateFormat
import java.util.*

class AudioPlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAudioPlayerBinding
    private lateinit var selectableTrack: Track
    private var mediaPlayer: MediaPlayer? = null
    private lateinit var playerState: PlayerState
    private var mainTreadHandler: Handler? = null


    override fun onPause() {
        super.onPause()
        pausePreviewTrack()
        mainTreadHandler?.post(changePlayButtonImage())
        changePlaybackProgress()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mainTreadHandler?.removeCallbacks(changePlaybackProgress())
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initializeComponents()
        setInActivityElementsValueOfTrack()
        prepareMediaPlayer()
        mainTreadHandler?.post(changePlayButtonImage())
        mainTreadHandler?.post(changePlaybackProgress())
    }

    private fun initializeComponents() {
        mediaPlayer = MediaPlayer()
        playerState = PlayerState.DEFAULT
        mainTreadHandler = Handler(Looper.getMainLooper())
        mediaPlayer = MediaPlayer()
        selectableTrack = if (VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(TrackAdapter.SELECTABLE_TRACK, Track::class.java)!!
        } else {
            intent.getParcelableExtra(TrackAdapter.SELECTABLE_TRACK)!!
        }
        binding.backButton.setOnClickListener {
            finish()
        }
        binding.playButton.setOnClickListener {
            when(playerState) {
                PlayerState.PLAYING -> {
                    pausePreviewTrack()
                    mainTreadHandler?.post(changePlayButtonImage())
                    mainTreadHandler?.post(changePlaybackProgress())
                }
                PlayerState.PAUSE, PlayerState.PREPARED -> {
                    startPreviewTrack()
                    mainTreadHandler?.post(changePlayButtonImage())
                    mainTreadHandler?.post(changePlaybackProgress())
                }
                PlayerState.DEFAULT -> {

                }
            }
        }
    }

    private fun changePlayButtonImage(): Runnable {
        return Runnable {
            when (playerState) {
                PlayerState.PLAYING -> {
                    binding.playButton.setImageResource(R.drawable.button_pause)
                }
                PlayerState.PAUSE, PlayerState.PREPARED -> {
                    binding.playButton.setImageResource(R.drawable.button_play)
                }
                PlayerState.DEFAULT -> {

                }
            }
        }
    }

    private fun changePlaybackProgress(): Runnable {
        return object : Runnable {
            override fun run() {
                when (playerState) {
                    PlayerState.PLAYING -> {
                        binding.currentTrackTimeAudioPlayer.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer?.currentPosition)
                        mainTreadHandler?.postDelayed(this, DELAY_CURRENT_TIME_1000_MILLIS)
                    }
                    PlayerState.PAUSE -> {
                        mainTreadHandler?.removeCallbacks(this)
                    }
                    PlayerState.PREPARED -> {
                        binding.currentTrackTimeAudioPlayer.text = "00:00"
                    }
                    PlayerState.DEFAULT -> {

                    }
                }
            }
        }
    }

    private fun prepareMediaPlayer() {
        mediaPlayer?.apply {
            setDataSource(selectableTrack.previewUrl)
            prepareAsync()
            setOnPreparedListener {
                playerState = PlayerState.PREPARED
            }
            setOnCompletionListener {
                playerState = PlayerState.PREPARED
                mainTreadHandler?.post(changePlayButtonImage())
            }
        }
    }

    private fun startPreviewTrack() {
        mediaPlayer?.start()
        playerState = PlayerState.PLAYING
    }

   private fun pausePreviewTrack() {
        mediaPlayer?.pause()
        playerState = PlayerState.PAUSE
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
    }

    enum class PlayerState {
        DEFAULT,
        PREPARED,
        PLAYING,
        PAUSE
    }

    companion object {
        private const val DELAY_CURRENT_TIME_1000_MILLIS = 1000L
    }

}