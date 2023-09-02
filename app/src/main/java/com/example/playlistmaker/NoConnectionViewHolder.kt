package com.example.playlistmaker

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewParent
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView.ViewHolder

class NoConnectionPlaceHolder(): ListItem

class NoConnectionViewHolder(parent: ViewGroup): ViewHolder(
    LayoutInflater.from(parent.context)
    .inflate(R.layout.no_connect_view,parent,false)
), ListItem {
    val updateButton: Button = itemView.findViewById(R.id.updateQueryButton)
}