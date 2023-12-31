package com.example.playlistmaker.ui.library.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.LibraryActivityFragmentPlaylistsBinding
import com.example.playlistmaker.ui.library.view_model.PlaylistsFragmentViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment: Fragment() {

    private var _binding: LibraryActivityFragmentPlaylistsBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<PlaylistsFragmentViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = LibraryActivityFragmentPlaylistsBinding.inflate(inflater,container,false)
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
        fun newInstance() = PlaylistsFragment()
    }
}