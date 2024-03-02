package com.example.playlistmaker.ui.audioplayer.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentAudioPlayerBinding
import com.example.playlistmaker.domain.library.playlists.model.Playlist
import com.example.playlistmaker.domain.library.playlists.model.PlaylistsState
import com.example.playlistmaker.domain.player.model.PlayerStatus
import com.example.playlistmaker.domain.search.model.Track
import com.example.playlistmaker.ui.audioplayer.view_model.PlayerFragmentViewModel
import com.example.playlistmaker.ui.library.model.PlaylistBottomSheetAdapter
import com.example.playlistmaker.ui.mapper.TrackMapper
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerFragment : Fragment() {

    private var _binding: FragmentAudioPlayerBinding? = null
    private val binding get() = _binding!!

    private lateinit var selectableTrack: Track

    private var playlists = ArrayList<Playlist>()

    private var playlistsAdapter: PlaylistBottomSheetAdapter? = null

    private val viewModel by viewModel<PlayerFragmentViewModel>()


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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAudioPlayerBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        selectableTrack = arguments?.getParcelable<Track>(RECEIVED_TRACK) as Track
        playlistsAdapter = PlaylistBottomSheetAdapter(playlists)
        binding.bottomPlaylistsRV.adapter = playlistsAdapter
        initializeComponents()
        setInActivityElementsValueOfTrack()
        observeOnPlayerStatusLiveData()
        observeOnIsFavoriteTrack()
        observeOnStatusPlaylists()
        viewModel.prepareMediaPlayer(selectableTrack)

    }

    fun observeOnStatusPlaylists() {
        viewModel.getPlaylistStatus().observe(viewLifecycleOwner) {
            when (it) {
                is PlaylistsState.Empty -> {
                    binding.bottomPlaylistsRV.visibility = View.GONE
                }
                is PlaylistsState.Content -> {
                    playlists.clear()
                    playlists.addAll(it.playlists)
                    playlistsAdapter?.notifyDataSetChanged()
                    binding.bottomPlaylistsRV.visibility = View.VISIBLE
                }
            }
        }
    }

    fun observeOnPlayerStatusLiveData() {
        viewModel.getStatusPlayer().observe(viewLifecycleOwner) {
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
        viewModel.getIsFavoriteTrack().observe(viewLifecycleOwner) {
            when (it) {
                true -> {
                        binding.favoriteButton.setImageResource(R.drawable.test_like)
                }
                false -> {
                    binding.favoriteButton.setImageResource(R.drawable.test_unlike)
                }
            }
        }
    }

    private fun initializeComponents() {
        if (selectableTrack.isFavorite) {
            binding.favoriteButton.setImageResource(R.drawable.test_like)
        }
        else {
            binding.favoriteButton.setImageResource(R.drawable.test_unlike)
        }

        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.playButton.setOnClickListener {
            if (selectableTrack.previewUrl.equals(getString(R.string.no_data))) {
                Toast.makeText(requireContext(), getString(R.string.can_not_play), Toast.LENGTH_LONG).show()
            } else {
                viewModel.clickOnPlayButton(selectableTrack)
            }
        }

        binding.favoriteButton.setOnClickListener {
            viewModel.onFavoriteClicked(selectableTrack)
        }

        val bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        binding.addToLibraryButton.setOnClickListener {
            binding.overlay.visibility = View.VISIBLE
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

        }
        binding.addNewPlaylist.setOnClickListener {
            //findNavController().navigate(R.id.action_audioPlayerFragment_to_Create)
        }

        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.visibility = View.GONE
                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        viewModel.selectAllPlaylistsFromDb()
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                binding.overlay.alpha = slideOffset
            }
        }
        )

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
        private const val RECEIVED_TRACK = "RECEIVED_TRACK"
    }

}