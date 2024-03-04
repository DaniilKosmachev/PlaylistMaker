package com.example.playlistmaker.ui.library.model

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.library.playlists.model.Playlist

class PlaylistViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    private val playlistName: TextView = itemView.findViewById(R.id.playlist_name_TV)
    private val countTracksInPlaylist: TextView = itemView.findViewById(R.id.count_tracks_in_playlist_TV)
    private val artworkPlaylist: ImageView = itemView.findViewById(R.id.playlist_artwork_IV)
    fun bind(item: Playlist) {
        playlistName.text = item.name
        countTracksInPlaylist.text = itemView.context.resources.getQuantityString(R.plurals.plurals_track_count, item.count!!, item.count!!)
        Glide.with(itemView)
            .load(item.uri?.toUri())
            .placeholder(R.drawable.placeholder)
            .centerCrop()
            .into(artworkPlaylist)
    }
}