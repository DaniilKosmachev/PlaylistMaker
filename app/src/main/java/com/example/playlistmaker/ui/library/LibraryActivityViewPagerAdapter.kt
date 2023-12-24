package com.example.playlistmaker.ui.library

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.playlistmaker.ui.library.fragments.FavoriteTracksFragment
import com.example.playlistmaker.ui.library.fragments.PlaylistsFragment

class LibraryActivityViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle):
    FragmentStateAdapter(fragmentManager, lifecycle)
{
    override fun getItemCount(): Int {
        return LIBRARY_ACTIVITY_TABS_COUNT
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FavoriteTracksFragment.newInstance()
            else -> PlaylistsFragment.newInstance()
        }
    }

    companion object {
        const val LIBRARY_ACTIVITY_TABS_COUNT = 2
    }
}