package com.example.playlistmaker.ui.playlist.fragment

import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistBinding
import com.example.playlistmaker.domain.library.favorite_tracks.model.FavoriteTracksState
import com.example.playlistmaker.domain.library.playlists.model.Playlist
import com.example.playlistmaker.domain.library.playlists.model.PlaylistsState
import com.example.playlistmaker.domain.search.model.Track
import com.example.playlistmaker.ui.library.model.PlaylistBottomSheetAdapter
import com.example.playlistmaker.ui.playlist.view_model.PlaylistFragmentViewModel
import com.example.playlistmaker.ui.search.TrackAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Locale
import java.util.concurrent.TimeUnit

class PlaylistFragment: Fragment() {

    private var tracksInPlaylist = ArrayList<Track>()

    private var _binding: FragmentPlaylistBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<PlaylistFragmentViewModel>()

    private var tracksAdapter: TrackAdapter? = null

    private var playlistAdapter: PlaylistBottomSheetAdapter? = null

    private var selectablePlaylist: Playlist? = null

    private var informationDialog: MaterialAlertDialogBuilder? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlaylistBinding.inflate(inflater,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        selectablePlaylist = arguments?.getParcelable<Playlist>(RECEIVED_PLAYLIST) as Playlist

        viewModel.selectAllTrackInPlaylist(selectablePlaylist?.id!!)

        selectablePlaylist!!.id?.let {
            viewModel.selectAllTrackInPlaylist(it)
        }

        viewModel.statusUpdatedPlaylist().observe(viewLifecycleOwner) {
            when(it) {
                is PlaylistsState.Content -> {
                    render(it.playlists.get(0))
                    selectablePlaylist = it.playlists[0]
                    Glide.with(binding.coverArtWorkImageIV)
                        .load(selectablePlaylist?.uri?.toUri())
                        .placeholder(R.drawable.placeholder)
                        .centerCrop()
                        .into(binding.coverArtWorkImageIV)
                }

                else -> {

                }
            }
        }

        viewModel.updatePlaylistInfo(selectablePlaylist!!.id!!)



        viewModel.statusTracksInPlaylist().observe(viewLifecycleOwner) {
            when(it) {
                is FavoriteTracksState.Empty -> {
                    binding.bottomTracksRV.isVisible = false
                    binding.emptyTracksListTV.isVisible = true
                    binding.durationOfPlaylistTV.text = "0 минут"
                    binding.countOfTracksInPlaylistTV.text = "0 треков"
                    tracksInPlaylist.clear()
                    tracksAdapter!!.notifyDataSetChanged()
                }
                is FavoriteTracksState.Content -> {
                    tracksInPlaylist.clear()
                    tracksInPlaylist.addAll(it.tracks)
                    binding.durationOfPlaylistTV.text = requireContext().resources.getQuantityString(R.plurals.plurals_tracks_duration, durationAllTracks(it.tracks).toInt(), durationAllTracks(it.tracks))
                    binding.countOfTracksInPlaylistTV.text = requireContext().resources.getQuantityString(R.plurals.plurals_track_count, it.tracks.size ?: 0, it.tracks.size ?: 0)
                    tracksAdapter!!.notifyDataSetChanged()
                    binding.bottomTracksRV.isVisible = true
                    binding.emptyTracksListTV.isVisible = false
                }

                else -> {

                }
            }
        }

        playlistAdapter = PlaylistBottomSheetAdapter(listOf(selectablePlaylist!!)) {

        }
        binding.bottomPlaylistMenuRV.adapter = playlistAdapter

        tracksAdapter = TrackAdapter(tracksInPlaylist, {
            openAudioPlayerAndReceiveTrackInfo(it)
        }, {
                showDialogAlet(it)
        })

        binding.bottomTracksRV.adapter = tracksAdapter

        binding.shareButtonIB.setOnClickListener {
            sharePlaylist()
        }

        var bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheetPlaylistMenu).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        binding.menuButtonIB.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        binding.linearSharePlaylist.setOnClickListener {
            sharePlaylist()
        }

        binding.deletePlaylist.setOnClickListener {
            deletePlaylist()
        }

        binding.editPlaylistInformation.setOnClickListener {
            openEditPlaylistFragmentAndReceivedPlaylist()
        }

        render(selectablePlaylist!!)
    }

    private fun openEditPlaylistFragmentAndReceivedPlaylist() {
        val bundle = Bundle()
        bundle.putParcelable(RECEIVED_PLAYLIST, selectablePlaylist!!)
        findNavController().navigate(R.id.action_playlistFragment_to_editPlaylistFragment, bundle)
    }

    private fun sharePlaylist() {
        if (!tracksInPlaylist.isNullOrEmpty()) {
            var message =
                "${selectablePlaylist!!.name}" +
                        "\n${if (selectablePlaylist!!.description.isNullOrEmpty()) "Нет описания" else selectablePlaylist!!.description}" +
                        "\n${requireContext().resources.getQuantityString(R.plurals.plurals_track_count, tracksInPlaylist.size, tracksInPlaylist.size)}"
            tracksInPlaylist.forEach { track ->
                message += "\n ${tracksInPlaylist.indexOf(track)+1}. ${track.artistName} - ${track.trackName} (${SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)})"
            }
            viewModel.sharePlaylist(message)
        } else {
            Toast.makeText(requireContext(), "В этом плейлисте нет списка треков, которым можно поделиться", Toast.LENGTH_SHORT).show()
        }
    }

    private fun deletePlaylist() {
        informationDialog = MaterialAlertDialogBuilder(requireContext()).apply {
            setTitle("Хотите удалить плейлист \"${selectablePlaylist!!.name}\"?")
            setPositiveButton("Да") { _,_ ->
                viewModel.deletePlaylist(selectablePlaylist!!.id!!)
                findNavController().navigateUp()
            }
            setNegativeButton("Нет") { _,_ ->

            }
        }
        informationDialog!!.show()
    }

    private fun durationAllTracks(tracks: List<Track>): Long {
        var sumLong = 0L
        tracks.forEach {
            track -> sumLong += track.trackTimeMillis ?: 0
        }
        var minutes = TimeUnit.MILLISECONDS.toMinutes(sumLong)
        return minutes
    }

    private fun showDialogAlet(track: Track) {
        informationDialog = MaterialAlertDialogBuilder(requireContext()).apply {
            setTitle("Хотите удалить трек?")
            setPositiveButton("Да") { _,_ ->
                selectablePlaylist?.id?.let { viewModel.deleteTrackFromDb(track.trackId, it) }
            }
            setNegativeButton("Нет") { _,_ ->

            }
        }
        informationDialog!!.show()
    }

    private fun render(playlist: Playlist) {
        binding.nameOfPlaylistTV.text = playlist.name
        binding.descriptionOfPlaylistTV.text = if (playlist.description.isNullOrEmpty()) "Нет данных" else playlist.description
    }

    private fun openAudioPlayerAndReceiveTrackInfo(track: Track) {
        val bundle = Bundle()
        bundle.putParcelable(RECEIVED_TRACK, track)
        findNavController().navigate(R.id.action_playlistFragment_to_audioPlayerFragment,bundle)
    }

    companion object {
        const val RECEIVED_PLAYLIST = "RECEIVED_PLAYLIST"
        const val RECEIVED_TRACK = "RECEIVED_TRACK"
    }

}