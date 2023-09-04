package com.example.playlistmaker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

class TrackAdapter (private val data: List<Track>): RecyclerView.Adapter<TrackViewHolder>() {
    override fun getItemCount() = data.size


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
    return TrackViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.track_view,parent,false))
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
      holder.bind(data[position])
    }
}