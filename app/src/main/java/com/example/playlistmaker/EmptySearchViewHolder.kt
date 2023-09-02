package com.example.playlistmaker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder

class EmptySearchPlaceHolder(): ListItem

class EmptySearchViewHolder(parent: ViewGroup): ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.empty_search_view,parent,false)) {

}