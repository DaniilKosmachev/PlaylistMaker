package com.example.playlistmaker

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson

class TrackAdapter(private val data: List<Track>, private val clickListener: trackClickListener) :
    RecyclerView.Adapter<TrackViewHolder>() {
    override fun getItemCount() = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        return TrackViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.track_view, parent, false)
        )
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(data[position])
        holder.itemView.setOnClickListener {
            clickListener.onTrackClickListener(data[position])
        }
    }


    fun interface trackClickListener {
        fun onTrackClickListener(track: Track)
    }

    companion object {
        const val SELECTABLE_TRACK = "selectable_track"
    }
}