package com.example.playlistmaker.ui.library.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistsBinding
import com.example.playlistmaker.domain.library.playlists.model.Playlist
import com.example.playlistmaker.domain.library.playlists.model.PlaylistsState
import com.example.playlistmaker.ui.library.model.PlaylistAdapter
import com.example.playlistmaker.ui.library.view_model.PlaylistsFragmentViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment: Fragment() {

    private var playlists = ArrayList<Playlist>()

    //private var tracksInPlaylists = ArrayList<TracksInPlaylists>()

    private var _binding: FragmentPlaylistsBinding? = null
    private val binding get() = _binding!!

    //private var bundleForListTracks: Bundle? = null

    private val viewModel by viewModel<PlaylistsFragmentViewModel>()

    private var playlistsAdapter: PlaylistAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlaylistsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        observeOnStatusAllPlaylists()
        playlistsAdapter?.notifyDataSetChanged()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeOnStatusAllPlaylists()
        viewModel.updateListPlaylistsFromDb()

        playlistsAdapter = PlaylistAdapter(playlists) {
            openPlaylistFragmentAndReceivePlaylist(it)
        }
        binding.playlistRV.adapter = playlistsAdapter

        playlistsAdapter?.notifyDataSetChanged()

        binding.addNewPlaylist.setOnClickListener {
            findNavController().navigate(R.id.action_libraryFragment_to_createPlaylistFragment)
        }
    }

    private fun observeOnStatusAllPlaylists() {
        viewModel.getStatusAllPlaylists().observe(viewLifecycleOwner) {
            when (it) {
                is PlaylistsState.Empty -> {
                    binding.emptyPlaylistsLayout.visibility = View.VISIBLE
                    binding.playlistsRecyclerViewLayout.visibility = View.GONE
                }
                is PlaylistsState.Content -> {
                    playlists.clear()
                    playlists.addAll(it.playlists)
                    playlistsAdapter?.notifyDataSetChanged()
                    binding.emptyPlaylistsLayout.visibility = View.GONE
                    binding.playlistsRecyclerViewLayout.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun openPlaylistFragmentAndReceivePlaylist(playlist: Playlist) {
        val bundle = Bundle()
        bundle.putParcelable(RECEIVED_PLAYLIST, playlist)
        findNavController().navigate(R.id.action_libraryFragment_to_playlistFragment,bundle)
    }

    companion object {
        fun newInstance() = PlaylistsFragment()
        const val RECEIVED_PLAYLIST = "RECEIVED_PLAYLIST"
    }
}