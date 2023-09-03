package com.example.playlistmaker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

class TrackAdapter (private val onUpdateButtonClickListener: OnUpdateButtonClickListener): RecyclerView.Adapter<ViewHolder>() {
    companion object {
        const val TRACK_LIST_ELEMENT_VIEW_TYPE = 0 //viewType если надо показать список Treck
        const val EMPTY_SEARCH_VIEW_TYPE = 1 //viewType если надо показать viewHolder при пустом ответе
        const val CONNECTION_ERROR_VIEW_TYPE = 2 //viewType если надо показать viewHolder при отсутствии связи
        const val VIEW_TYPE_UNKNOWN = 3
    }
    var items: MutableList<TrackAdapterListItem> = mutableListOf() //лист для хранения либо списка треков, либо объектов ошибочных результатов (пустой ответ, сеть)
    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder { //возвращает либой ViewHolder
    return when (viewType) { //в зависимости от типа принятой константы отрисовываем определенный ViewHolder
        TRACK_LIST_ELEMENT_VIEW_TYPE -> TrackViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.track_view,parent,false))
        EMPTY_SEARCH_VIEW_TYPE -> EmptySearchViewHolder(parent)
        CONNECTION_ERROR_VIEW_TYPE-> NoConnectionViewHolder(parent)
        else -> throw java.lang.IllegalStateException("Что-то вообще идет не так")
    }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (val item = items[position]) {
            is Track -> { //в зависимости от элементов списка привязываем элементы к их значениям
                (holder as TrackViewHolder).bind(item)
            }
            is NoConnectionPlaceHolder -> { //здесь же устанавливаем слушаетль на кнопку Обновить при отсутствии сети
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