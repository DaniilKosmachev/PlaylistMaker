package com.example.playlistmaker.ui.library.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentFavoriteTracksBinding
import com.example.playlistmaker.domain.library.favorite_tracks.model.FavoriteTracksState
import com.example.playlistmaker.domain.search.model.Track
import com.example.playlistmaker.ui.library.view_model.FavoriteTrackFragmentViewModel
import com.example.playlistmaker.ui.search.TrackAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteTracksFragment: Fragment() {

    private val favoriteTracks = ArrayList<Track>()

    private var _binding: FragmentFavoriteTracksBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<FavoriteTrackFragmentViewModel>()

    private var favoriteTrackAdapter: TrackAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoriteTracksBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("TEST","Произошел onDestroyView")
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        Log.d("TEST","Произошел onResume")
        viewModel.updateListFromDb()
        initializedComponent()
        observeOnFavoriteTrack()
        favoriteTrackAdapter?.notifyDataSetChanged()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("TEST", "Произошел onViewCreated")
        initializedComponent()
        observeOnFavoriteTrack()
        favoriteTrackAdapter?.notifyDataSetChanged()
    }

    fun observeOnFavoriteTrack() {
        viewModel.getStatusFavoriteTracks().observe(viewLifecycleOwner) {
            when (it) {
                is FavoriteTracksState.Empty -> {
                    binding.emptyMediaLayout.isVisible = true
                    binding.trackRecyclerLayout.isVisible = false
                }

                is FavoriteTracksState.Content -> {
                    favoriteTracks.clear()
                    favoriteTracks.addAll(it.tracks)
                    binding.emptyMediaLayout.isVisible = false
                   favoriteTrackAdapter?.notifyDataSetChanged()
                    binding.trackRecyclerLayout.isVisible = true
                }
            }
        }
    }

    fun initializedComponent() {
        favoriteTrackAdapter = TrackAdapter(favoriteTracks) {
            openAudioPlayerAndReceiveTrackInfo(it)
        }
        binding.favoriteTrackRecycleView.adapter = favoriteTrackAdapter
    }

    private fun openAudioPlayerAndReceiveTrackInfo(track: Track) {
//        Intent(requireContext(), AudioPlayerFragment::class.java).apply {
//            putExtra(TrackAdapter.SELECTABLE_TRACK, track)
//            startActivity(this)
//        }
        val bundle = Bundle()
        bundle.putParcelable(RECEIVED_TRACK, track)
        findNavController().navigate(R.id.action_libraryFragment_to_audioPlayerFragment,bundle)
    }

    companion object {
        fun newInstance() = FavoriteTracksFragment()
        private const val RECEIVED_TRACK = "RECEIVED_TRACK"
    }
}