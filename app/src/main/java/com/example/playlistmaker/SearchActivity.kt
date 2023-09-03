package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity(), TrackAdapter.OnUpdateButtonClickListener {//реализуем интерфейс, написанный в TrackAdapter
    private val iTunesBaseUrl = "https://itunes.apple.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val itunesService = retrofit.create(ITunesApi::class.java)

    val iTunesTrack = ArrayList<Track>()//список треков из iTunes

    var searchQueryText = ""

    private lateinit var trackAdapter: TrackAdapter
    private lateinit var editTextSearch: EditText
    private lateinit var buttonBack:ImageButton
    private lateinit var clearButton: ImageButton
    companion object {
        private const val SEARCH_STRING = "SEARCH_STRING"
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_STRING, searchQueryText)
    }

   override fun onRestoreInstanceState(savedInstanceState: Bundle) {
       val editTextSearch = findViewById<EditText>(R.id.editTextSearchActivity)
        super.onRestoreInstanceState(savedInstanceState)
        editTextSearch.setText(searchQueryText)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        buttonBack = findViewById(R.id.backToMainActivityViewButtonFromSearch)
        editTextSearch = findViewById(R.id.editTextSearchActivity)
        clearButton = findViewById(R.id.clearEditTextSearchActivity)
        clearButton.setOnClickListener {//кнопка очистки
            editTextSearch.setText("")//устанавливает текст пустой
            iTunesTrack.clear()//очищаем список треков
            trackAdapter.items.clear()//очищаем список items, чтобы не было элементов для отображения
            trackAdapter.notifyDataSetChanged()//полностью перерисовываем адаптер
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(editTextSearch.windowToken,0)
        }
        buttonBack.setOnClickListener {
            finish()
        }
        val searchActivityTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = clearButtonVisibility(s)
                searchQueryText = editTextSearch.text.toString()
            }

            override fun afterTextChanged(s: Editable?) {

            }
            fun clearButtonVisibility (s: CharSequence?): Int {
                return if (s.isNullOrEmpty()) {
                    View. GONE
                }
                else {
                    View.VISIBLE
                }
            }
        }

        editTextSearch.addTextChangedListener(searchActivityTextWatcher)

        trackAdapter = TrackAdapter(this)

        val recycleViewTrack = findViewById<RecyclerView>(R.id.track_recycle_view)

        recycleViewTrack.adapter = trackAdapter

        editTextSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (searchQueryText.isNotEmpty()) {
                    startSearchTrack()//запускаем метод поиска треков
                }
                true
            } else
            {
                false
            }
        }
    }
    private fun startSearchTrack() {
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(editTextSearch.windowToken,0)//сразу сворачиваем клавиатуру - не удобно с ней
        itunesService.search(searchQueryText)
            .enqueue(object : Callback<TracksResponse> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(
                    call: Call<TracksResponse>,
                    response: Response<TracksResponse>
                ) {
                    if (response.code() == 200) {
                        trackAdapter.items.clear()//если код 200 - очищаем список items viewHolder'ов
                        iTunesTrack.clear()//на всякий очищаем список треков
                        if (response.body()?.results?.isNotEmpty() == true) {
                            iTunesTrack.addAll(response.body()?.results!!)//если ответ не пуст - results (лист типа Track - ответ от сервера)
                            trackAdapter.items.addAll(iTunesTrack)//добавляем iTunesTrack в items, чтобы отрисовался viewHolder для Track
                            trackAdapter.notifyDataSetChanged()//полностью перерисовываем адаптер
                        }
                        if (iTunesTrack.isEmpty()) {
                            trackAdapter.items.clear()//если пустой - очищаем список items адаптера
                            trackAdapter.items.add(EmptySearchPlaceHolder())//помещаем в этот список класс пустого результата (он в EmptySearchViewHolder написан)
                            trackAdapter.notifyDataSetChanged()//перерисовываем адаптер
                        }
                    }
                }

                @SuppressLint("NotifyDataSetChanged")
                override fun onFailure(call: Call<TracksResponse>, t: Throwable) {
                    iTunesTrack.clear()//при ошибке - очищаем список треков
                    trackAdapter.items.clear()//очищаем items для отображения
                    trackAdapter.items.add(NoConnectionPlaceHolder())//помещаем в этот список класс ошибок сети (он в NoConnectionViewHolder написан)
                    trackAdapter.notifyDataSetChanged()//перерисовываем
                }

            })
    }
       override fun onUpdateButtonClickListener() { //реализуем метод интерфейса
           startSearchTrack()
    }
}