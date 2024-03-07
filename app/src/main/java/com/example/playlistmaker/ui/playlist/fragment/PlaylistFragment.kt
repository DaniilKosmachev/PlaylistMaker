package com.example.playlistmaker.ui.playlist.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistBinding
import com.example.playlistmaker.domain.library.favorite_tracks.model.FavoriteTracksState
import com.example.playlistmaker.domain.library.playlists.model.Playlist
import com.example.playlistmaker.domain.search.model.Track
import com.example.playlistmaker.ui.playlist.view_model.PlaylistFragmentViewModel
import com.example.playlistmaker.ui.search.TrackAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.concurrent.TimeUnit

class PlaylistFragment: Fragment() {

    private var tracksInPlaylist = ArrayList<Track>()

    private var _binding: FragmentPlaylistBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<PlaylistFragmentViewModel>()

    private var tracksAdapter: TrackAdapter? = null

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
            }
        }

        tracksAdapter = TrackAdapter(tracksInPlaylist, {
            openAudioPlayerAndReceiveTrackInfo(it)
        }, {
                showDialogAlet(it)
        })

        binding.bottomTracksRV.adapter = tracksAdapter

        render(selectablePlaylist!!)

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
        Glide.with(binding.coverArtWorkImageIV)
            .load(selectablePlaylist?.uri?.toUri())
            .placeholder(R.drawable.placeholder)
            .centerCrop()
            .into(binding.coverArtWorkImageIV)
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