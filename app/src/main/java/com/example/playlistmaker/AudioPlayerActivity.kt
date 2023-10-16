package com.example.playlistmaker

import android.media.MediaPlayer
import android.os.Build
import android.os.Build.VERSION
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.databinding.ActivityAudioPlayerBinding
import java.text.SimpleDateFormat
import java.util.*

class AudioPlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAudioPlayerBinding
    private lateinit var selectableTrack: Track
    private lateinit var mediaPlayer: MediaPlayer
    private var playerState = PLAYER_DEFAULT_STATE
    private var mainTreadHandler: Handler? = null


    override fun onPause() {
        super.onPause()
        pausePreviewTrack()
        mainTreadHandler?.post(changePlayButtonImage())
        changePlaybackProgress()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
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
                PLAYER_PLAYING_STATE -> {
                    pausePreviewTrack()
                    mainTreadHandler?.post(changePlayButtonImage())
                    mainTreadHandler?.post(changePlaybackProgress())
                }
                PLAYER_PREPARED_STATE, PLAYER_PAUSE_STATE -> {
                    startPreviewTrack()
                    mainTreadHandler?.post(changePlayButtonImage())
                    mainTreadHandler?.post(changePlaybackProgress())
                }
            }
        }
    }

    private fun changePlayButtonImage(): Runnable {
        return Runnable {
            when (playerState) {
                PLAYER_PLAYING_STATE -> {
                    binding.playButton.setImageResource(R.drawable.button_pause)
                }
                PLAYER_PREPARED_STATE, PLAYER_PAUSE_STATE -> {
                    binding.playButton.setImageResource(R.drawable.button_play)
                }
            }
        }
    }

    private fun changePlaybackProgress(): Runnable {
        return object : Runnable {
            override fun run() {
                when (playerState) {
                    PLAYER_PLAYING_STATE -> {
                        binding.currentTrackTimeAudioPlayer.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)
                        mainTreadHandler?.postDelayed(this, DELAY_CURRENT_TIME_1000)
                    }
                    PLAYER_PAUSE_STATE -> {
                        mainTreadHandler?.removeCallbacks(this)
                    }
                    PLAYER_PREPARED_STATE -> {
                        binding.currentTrackTimeAudioPlayer.text = "00:00"
                    }
                }
            }
        }
    }

    private fun prepareMediaPlayer() {
        mediaPlayer.apply {
            setDataSource(selectableTrack.previewUrl)
            prepareAsync()
            setOnPreparedListener {
                playerState = PLAYER_PREPARED_STATE
            }
            setOnCompletionListener {
                playerState = PLAYER_PREPARED_STATE
                mainTreadHandler?.post(changePlayButtonImage())
            }
        }
    }

    private fun startPreviewTrack() {
        mediaPlayer.start()
        playerState = PLAYER_PLAYING_STATE
    }

   private fun pausePreviewTrack() {
        mediaPlayer.pause()
        playerState = PLAYER_PAUSE_STATE
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

    companion object {
        private const val DELAY_CURRENT_TIME_1000 = 1000L
        private const val PLAYER_DEFAULT_STATE = 0
        private const val PLAYER_PREPARED_STATE = 1
        private const val PLAYER_PLAYING_STATE = 2
        private const val PLAYER_PAUSE_STATE = 3
    }

}