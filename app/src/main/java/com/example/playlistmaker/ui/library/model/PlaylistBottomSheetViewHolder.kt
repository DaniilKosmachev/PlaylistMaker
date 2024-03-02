package com.example.playlistmaker.ui.library.model

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.library.playlists.model.Playlist

class PlaylistBottomSheetViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    private val playlistName: TextView = itemView.findViewById(R.id.playlist_name_bottom_TV)
    private val countTracks: TextView = itemView.findViewById(R.id.playlists_track_count_bottom_TV)
    private val artworkPlaylist: ImageView = itemView.findViewById(R.id.playlist_artwork_bottom_IV)

    fun bind(item: Playlist) {
        playlistName.text = item.name
        countTracks.text = item.count.toString() + " треков"
        Glide.with(itemView)
            .load(item.uri?.toUri())
            .placeholder(R.drawable.placeholder)
            .centerCrop()
            .into(artworkPlaylist)
    }

}