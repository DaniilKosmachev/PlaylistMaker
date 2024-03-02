package com.example.playlistmaker.ui.library.model

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.library.playlists.model.Playlist

class PlaylistBottomSheetAdapter(private var data: List<Playlist>): RecyclerView.Adapter<PlaylistBottomSheetViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlaylistBottomSheetViewHolder {
        return PlaylistBottomSheetViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.playlist_bottom_view,parent,false))
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: PlaylistBottomSheetViewHolder, position: Int) {
        holder.bind(data[position])
    }


}