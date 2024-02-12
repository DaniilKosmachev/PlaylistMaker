package com.example.playlistmaker.ui.library.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentFavoriteTracksBinding
import com.example.playlistmaker.domain.library.favorite_tracks.model.FavoriteTracksState
import com.example.playlistmaker.domain.search.model.Track
import com.example.playlistmaker.ui.audioplayer.activity.AudioPlayerActivity
import com.example.playlistmaker.ui.library.view_model.FavoriteTrackFragmentViewModel
import com.example.playlistmaker.ui.search.TrackAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteTracksFragment: Fragment() {

    private val favoriteTracks = ArrayList<Track>()

    private var _binding: FragmentFavoriteTracksBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<FavoriteTrackFragmentViewModel>()

    private lateinit var favoriteTrackAdapter: TrackAdapter

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

    override fun onStop() {
        super.onStop()
        Log.d("TEST","Произошел onStop - $_binding")
    }

    override fun onResume() {
        super.onResume()
        Log.d("TEST","Произошел onResume")
        viewModel.updateListFromDb()
        initializedComponent()
        observeOnFavoriteTrack()
        favoriteTrackAdapter.notifyDataSetChanged()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("TEST", "Произошел onViewCreated")
        initializedComponent()
        observeOnFavoriteTrack()
        favoriteTrackAdapter.notifyDataSetChanged()
    }

    fun observeOnFavoriteTrack() {
        viewModel.getStatusFavoriteTracks().observe(viewLifecycleOwner) {
            when (it) {
                is FavoriteTracksState.Empty -> {
                    binding.emptyMediaLayout.visibility = View.VISIBLE
                    binding.trackRecyclerLayout.visibility = View.GONE
                }

                is FavoriteTracksState.Content -> {
                    favoriteTracks.clear()
                    favoriteTracks.addAll(it.tracks)
                    binding.emptyMediaLayout.visibility = View.GONE
                   favoriteTrackAdapter.notifyDataSetChanged()
                    binding.trackRecyclerLayout.visibility = View.VISIBLE
                }
            }
        }
    }

    fun initializedComponent() {
        Log.d("TEST", "Адаптер")
        favoriteTrackAdapter = TrackAdapter(favoriteTracks) {
            openAudioPlayerAndReceiveTrackInfo(it)
        }
        binding.favoriteTrackRecycleView.adapter = favoriteTrackAdapter
    }

    private fun openAudioPlayerAndReceiveTrackInfo(track: Track) {
        Intent(requireContext(), AudioPlayerActivity::class.java).apply {
            putExtra(TrackAdapter.SELECTABLE_TRACK, track)
            startActivity(this)
        }
    }

    companion object {
        fun newInstance() = FavoriteTracksFragment()
    }
}