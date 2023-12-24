package com.example.playlistmaker.ui.library.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.LibraryActivityFragmentFavoriteTracksBinding
import com.example.playlistmaker.ui.library.view_model.FavoriteTrackFragmentViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteTracksFragment: Fragment() {

    private var _binding: LibraryActivityFragmentFavoriteTracksBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<FavoriteTrackFragmentViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = LibraryActivityFragmentFavoriteTracksBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        fun newInstance() = FavoriteTracksFragment()
    }
}