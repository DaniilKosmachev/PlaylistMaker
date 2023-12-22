package com.example.playlistmaker.ui.library.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.LibraryActivityFragmentPlaylistsBinding

class LibActivityPlaylistsFragment: Fragment() {

    private var _binding: LibraryActivityFragmentPlaylistsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = LibraryActivityFragmentPlaylistsBinding.inflate(inflater,container,false)
        return binding.root
    }

    companion object {
        fun newInstance() = LibActivityPlaylistsFragment()
    }
}