package com.example.playlistmaker.ui.audioplayer.activity

import android.os.Build
import android.os.Build.VERSION
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.example.playlistmaker.domain.player.model.PlayerStatus
import com.example.playlistmaker.domain.search.model.Track
import com.example.playlistmaker.ui.audioplayer.view_model.PlayerActivityViewModel
import com.example.playlistmaker.ui.mapper.TrackMapper
import com.example.playlistmaker.ui.search.TrackAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAudioPlayerBinding

    private lateinit var selectableTrack: Track

    private val viewModel by viewModel<PlayerActivityViewModel>()


    override fun onPause() {
        super.onPause()
        viewModel.pausePlay()
        viewModel.checkPlaybackProgressAndStatus()
    }

    override fun onDestroy() {
        viewModel.removeCallback()
        viewModel.destroyPlayer()
        super.onDestroy()

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
        initializeComponents()
        setInActivityElementsValueOfTrack()
        observeOnPlayerStatusLiveData()
        observeOnIsFavoriteTrack()
        viewModel.prepareMediaPlayer(selectableTrack)
    }

    fun observeOnPlayerStatusLiveData() {
        viewModel.getStatusPlayer().observe(this) {
            when (it.playerState) {
                PlayerStatus.PLAYING -> {
                    binding.currentTrackTimeAudioPlayer.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(it.currentPosition)
                    binding.playButton.setImageResource(R.drawable.button_pause)
                }
                PlayerStatus.PAUSE -> {
                    binding.playButton.setImageResource(R.drawable.button_play)
                }
                PlayerStatus.PREPARED -> {
                    binding.playButton.setImageResource(R.drawable.button_play)
                    binding.currentTrackTimeAudioPlayer.text =
                        getString(R.string.zero_playback_progress)
                }
                PlayerStatus.DEFAULT -> {
                    binding.playButton.setImageResource(R.drawable.button_play)
                }
            }
            }
        }

    fun observeOnIsFavoriteTrack() {
        viewModel.getIsFavoriteTrack().observe(this) {
            when (it) {
                true -> {
                    binding.favoriteButton.setImageResource(R.drawable.is_favorite_track)
                }
                false -> {
                    binding.favoriteButton.setImageResource(R.drawable.favoritetrackbutton)
                }
            }
        }
    }

    private fun initializeComponents() {

        if (selectableTrack.isFavorite) {
            binding.favoriteButton.setImageResource(R.drawable.is_favorite_track)
        }
        else {
            binding.favoriteButton.setImageResource(R.drawable.favoritetrackbutton)
        }

        binding.backButton.setOnClickListener {
            finish()
        }

        binding.playButton.setOnClickListener {
            if (selectableTrack.previewUrl.equals(getString(R.string.no_data))) {
                Toast.makeText(this, getString(R.string.can_not_play), Toast.LENGTH_LONG).show()
            } else {
                viewModel.clickOnPlayButton(selectableTrack)
            }
        }

        binding.favoriteButton.setOnClickListener {
            viewModel.onFavoriteClicked(selectableTrack)
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

}