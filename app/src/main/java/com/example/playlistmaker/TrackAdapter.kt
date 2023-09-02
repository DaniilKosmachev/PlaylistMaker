package com.example.playlistmaker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

class TrackAdapter (val onUpdateButtonClickListener: OnUpdateButtonClickListener): RecyclerView.Adapter<ViewHolder>() {
    companion object {
        const val TRACK_LIST_ELEMENT_VIEW_TYPE = 0
        const val EMPTY_SEARCH_VIEW_TYPE = 1
        const val CONNECTION_ERROR_VIEW_TYPE = 2
        const val VIEW_TYPE_UNKNOWN = 3
    }
    var items: MutableList<ListItem> = mutableListOf()
    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int): Int {
        val item = items[position]
        return when (item) {
            is Track -> {
                TRACK_LIST_ELEMENT_VIEW_TYPE
            }
            is EmptySearchPlaceHolder -> {
                EMPTY_SEARCH_VIEW_TYPE
            }
            is NoConnectionPlaceHolder -> {
                CONNECTION_ERROR_VIEW_TYPE
            }
            else -> {
                VIEW_TYPE_UNKNOWN
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    return when (viewType) {
        TRACK_LIST_ELEMENT_VIEW_TYPE -> TrackViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.track_view,parent,false))
        EMPTY_SEARCH_VIEW_TYPE -> EmptySearchViewHolder(parent)
        CONNECTION_ERROR_VIEW_TYPE-> NoConnectionViewHolder(parent)
        else -> throw java.lang.IllegalStateException("Что-то вообще идет не так")
    }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (val item = items[position]) {
            is Track -> {
                (holder as TrackViewHolder).bind(item)
            }
            is NoConnectionPlaceHolder -> {
                (holder as NoConnectionViewHolder).apply {
                    updateButton.setOnClickListener {
                        onUpdateButtonClickListener.onUpdateButtonClickListener()
                    }
                }
            }
            else -> {
                // nothing
            }
        }
    }
    interface OnUpdateButtonClickListener {
        fun onUpdateButtonClickListener()
    }
}