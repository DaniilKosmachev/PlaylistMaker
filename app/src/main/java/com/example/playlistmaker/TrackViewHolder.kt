package com.example.playlistmaker

import android.content.Context
import android.icu.text.ListFormatter.Width
import android.icu.text.SimpleDateFormat
import android.util.Log
import android.util.TypedValue
import android.view.RoundedCorner
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.util.*

class TrackViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    private val trackName: TextView = itemView.findViewById(R.id.track_name)
    private val artistName: TextView = itemView.findViewById(R.id.artist_name)
    private val trackTime: TextView = itemView.findViewById(R.id.track_time)
    private val artworkImage: ImageView = itemView.findViewById(R.id.art_work_img100)
    private val layoutArtistAndTime: LinearLayout = itemView.findViewById(R.id.layout_artist_and_time)
    fun bind(item: Track) {
        //layoutArtistAndTime.layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT
        //artistName.layoutParams.width = 0
        trackName.text = item.trackName
        artistName.text = item.artistName
        trackTime.text = SimpleDateFormat("mm:ss",Locale.getDefault()).format(item.trackTimeMillis)
        Glide.with(itemView)
            .load(item.artworkUrl100)
            .placeholder(R.drawable.placeholder)
            .centerCrop()
            .transform(RoundedCorners(itemView.resources.getDimensionPixelSize(R.dimen.image_artwork_corner_radius)))
            .into(artworkImage)
    }

}