package com.example.playlistmaker.ui.library.model

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.library.playlists.model.Playlist

class PlaylistAdapter(
    private val data: List<Playlist>,
    private val clickListener: playlistGridLayoutClickListener
): RecyclerView.Adapter<PlaylistViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        return PlaylistViewHolder(LayoutInflater
            .from(parent.context)
            .inflate(R.layout.playlist_view,parent,false))
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.bind(data[position])
        holder.itemView.setOnClickListener {
            clickListener.onPlaylistClickListener(data[position])
        }
    }
    fun interface playlistGridLayoutClickListener {
        fun onPlaylistClickListener(playlist: Playlist)
    }
}