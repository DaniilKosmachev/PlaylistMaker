package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.LinearLayout.GONE
import android.widget.LinearLayout.LayoutParams
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Retrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {
companion object {
    private const val SEARCH_STRING = "SEARCH_STRING"
    const val ITUNES_BASE_URL = "https://itunes.apple.com"
    const val SHARED_PREFERENCES_HISTORY_SEARCH_FILE_NAME = "history_search_track"
}

   private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(ITUNES_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val itunesService = retrofit.create(ITunesApi::class.java)

    private val iTunesTrack = ArrayList<Track>()//список треков из iTunes
    private val iTunesTrackSearchHistory = ArrayList<Track>()//список истории поиска

    var searchQueryText = ""

    private lateinit var trackHistoryAdapter: TrackAdapter
    private lateinit var trackAdapter: TrackAdapter
    private lateinit var editTextSearch: EditText
    private lateinit var buttonBack:ImageButton
    private lateinit var clearButton: ImageButton
    private lateinit var recycleViewTrack: RecyclerView
    private lateinit var updateButtonClick: Button
    private lateinit var errorLayout: LinearLayout
    private lateinit var errorImage: ImageView
    private lateinit var errorText: TextView
    private lateinit var recyclerViewHistoryTrack: RecyclerView
    private lateinit var historyLayout: LinearLayout
    private lateinit var historySharedPreferences: SharedPreferences
    private lateinit var classHistorySearch: TrackSearchHistory
    private lateinit var clearHistorySearchButton: Button

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
        initializedViewElementSearchActivity()
        clickOnClearButton()
        clickOnButtonBack()
        setSearchActivityTextWatcher()
        clickOnButtonDoneSystemKeyboard()
        onUpdateButtonClickListener()
        initizlizedComponents()
        setOnEditTextFocusLisneter()
        readOnHistoryTrackList()
        clickOnClearSearchHistoryButton()
    }

    private fun initizlizedComponents() {
        historySharedPreferences = getSharedPreferences(SHARED_PREFERENCES_HISTORY_SEARCH_FILE_NAME, MODE_PRIVATE)//инициализируем экземпляр SP
        classHistorySearch = TrackSearchHistory(historySharedPreferences)
        trackAdapter = TrackAdapter(iTunesTrack) {//адаптер для текущего поискового запроса
            addNewTrackInTrackHistory(it)
        }
        recycleViewTrack.adapter = trackAdapter//устанавливаем для RecyclerView текущего результата поиска адаптер
        trackHistoryAdapter = TrackAdapter(iTunesTrackSearchHistory) {
            showToast(it)
        }//адптер для истории поиска
        recyclerViewHistoryTrack.adapter=trackHistoryAdapter//устанавливаем для RecyclerView истории поиска адаптер
    }

    @SuppressLint("NotifyDataSetChanged")
    fun readOnHistoryTrackList() {//читаем из sheared preferences файла историю поиска
        iTunesTrackSearchHistory.addAll(classHistorySearch.getTrackArrayFromShared())
        trackHistoryAdapter.notifyDataSetChanged()
    }

    fun showToast(track: Track) {//просто тест на кликл истории поиска
        Toast.makeText(this@SearchActivity, "Нажата в истории - ${track.artistName}, ${track.trackName}",Toast.LENGTH_SHORT).show()
    }


    private fun addNewTrackInTrackHistory(newTrack: Track) {
        val temporaryTrackArray = ArrayList<Track>()
        temporaryTrackArray.addAll(classHistorySearch.getTrackArrayFromShared())//получаем десериализованный список треков из SP во временный лист
        if (temporaryTrackArray.isEmpty()) {//если он пуст, то просто добавляем новый трек на нулевой индекс
            iTunesTrackSearchHistory.add(0,newTrack)
        } else if (temporaryTrackArray.isNotEmpty()) {
            if (temporaryTrackArray.size == 11) {
                temporaryTrackArray.removeAt(10)
            }
            val iterator: MutableIterator<Track> = temporaryTrackArray.iterator()
            while (iterator.hasNext()) {
                val currentTrack = iterator.next()
                if (currentTrack.trackId == newTrack.trackId) {
                    iterator.remove()
                }
            }
            temporaryTrackArray.add(0,newTrack)
            iTunesTrackSearchHistory.clear()
            iTunesTrackSearchHistory.addAll(temporaryTrackArray)
        }
        classHistorySearch.writeTrackArrayToShared(iTunesTrackSearchHistory)//записываем итоговый массив в SP
    }

    private fun initializedViewElementSearchActivity() {
        buttonBack = findViewById(R.id.backToMainActivityViewButtonFromSearch)
        editTextSearch = findViewById(R.id.editTextSearchActivity)
        clearButton = findViewById(R.id.clearEditTextSearchActivity)
        recycleViewTrack = findViewById(R.id.track_recycle_view)
        updateButtonClick = findViewById(R.id.updateQueryButton)
        errorLayout = findViewById(R.id.search_activity_error_linear_layout)
        errorImage = findViewById(R.id.search_activity_error_image)
        errorText = findViewById(R.id.search_activity_error_text)
        recyclerViewHistoryTrack = findViewById(R.id.search_activity_history_recycler_view)
        historyLayout = findViewById(R.id.search_activity_history_track_linear_layout)
        clearHistorySearchButton = findViewById(R.id.clear_search_history)
    }

    private fun setSearchActivityTextWatcher() {
        val searchActivityTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = clearButtonVisibility(s)
                searchQueryText = editTextSearch.text.toString()
                historyLayout.visibility = historyLinearLayoutVisibility(s)
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
            fun historyLinearLayoutVisibility (s: CharSequence?): Int {
                return if (s.isNullOrEmpty()) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
            }
        }
        editTextSearch.addTextChangedListener(searchActivityTextWatcher)
    }
    @SuppressLint("NotifyDataSetChanged")
    private fun setOnEditTextFocusLisneter() {
        editTextSearch.setOnFocusChangeListener { _, hasFocus ->
            if (iTunesTrackSearchHistory.isNotEmpty()) {
            historyLayout.visibility = if (hasFocus && editTextSearch.text.isNullOrEmpty()) View.VISIBLE else View.GONE
            trackHistoryAdapter.notifyDataSetChanged()
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun clickOnClearSearchHistoryButton() {
        clearHistorySearchButton.setOnClickListener {
            classHistorySearch.clearSearchHistory()
            iTunesTrackSearchHistory.clear()
            trackHistoryAdapter.notifyDataSetChanged()
            historyLayout.visibility = GONE
            trackHistoryAdapter.notifyDataSetChanged()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun clickOnClearButton() {
        clearButton.setOnClickListener {//кнопка очистки
            editTextSearch.setText("")//устанавливает текст пустой
            iTunesTrack.clear()//очищаем список треков
            trackAdapter.notifyDataSetChanged()//полностью перерисовываем адаптер
            hideSystemKeyboard()
            recycleViewTrack.isVisible = false
            errorLayout.isVisible = false
            trackHistoryAdapter.notifyDataSetChanged()//обновляем историю поиска
        }
    }

    private fun clickOnButtonBack() {
        buttonBack.setOnClickListener {
            finish()
        }
    }

    private fun clickOnButtonDoneSystemKeyboard() {
        editTextSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (searchQueryText.isNotEmpty()) {
                    startSearchTrack()//запускаем метод поиска треков
                }
                true
            } else {
                false
            }
        }
    }

    private fun hideSystemKeyboard() {
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(editTextSearch.windowToken,0)
    }

    private fun isPortrainSystemOrientatin(): Boolean {//true - если портретная
        return resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
}

    private fun isNightModeOn(): Boolean {//если дневная - false, ночная - true
        return when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> {
                true
            }
            Configuration.UI_MODE_NIGHT_NO ->  {
                false
            }
            Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                true
            }
            else -> true
        }
    }

    private fun setEmptyResponse() { //устанавливает в ErrorLinearLayout картинку и текст по ошибке - пустой ответ
        recycleViewTrack.isVisible = false
        errorLayout.isVisible = true
        updateErrorlayoutParamsForLandscapeOrientation()
        errorText.text = getString(R.string.empty_search)
        if (isNightModeOn()) {
            errorImage.setImageResource(R.drawable.emptyseacrhresultdark)
        } else {
            errorImage.setImageResource(R.drawable.emptysearchresultlight)
        }
        updateButtonClick.isVisible = false
    }

    private fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics).toInt()
    }

    private fun updateErrorlayoutParamsForLandscapeOrientation() {
        val params: LayoutParams =
            errorLayout.layoutParams as LayoutParams
        params.setMargins(0, dpToPx((if (isPortrainSystemOrientatin()) 102f else 8f),this),0,0)
    }

    private fun setNoConnectionError() { //устанавливает в ErrorLinearLayout картинку и текст по ошибке - отсутствует связь
        recycleViewTrack.isVisible = false
        errorLayout.isVisible = true
        updateErrorlayoutParamsForLandscapeOrientation()
        if (!isPortrainSystemOrientatin()) {//если не портретная ориентация, то заменяю все знаки новой строки, чтобы влезло в книжную ориентацию
        errorText.text = getString(R.string.connection_error).replace("\n"," ")
        } else {
            errorText.text = getString(R.string.connection_error)
        }
        if (isNightModeOn()) {
            errorImage.setImageResource(R.drawable.inteneterrordark)
        } else {
            errorImage.setImageResource(R.drawable.interneterrorlight)
        }
        updateButtonClick.isVisible = true
        hideSystemKeyboard()
    }

    private fun startSearchTrack() {
        itunesService.search(searchQueryText)
            .enqueue(object : Callback<TracksResponse> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(
                    call: Call<TracksResponse>,
                    response: Response<TracksResponse>
                ) {
                    if (response.code() == 200) { //если код 200 - очищаем список items viewHolder'ов
                        iTunesTrack.clear()//на всякий очищаем список треков
                        if (response.body()?.results?.isNotEmpty() == true) {
                            errorLayout.isVisible = false
                            recycleViewTrack.isVisible = true
                            hideSystemKeyboard()
                            iTunesTrack.addAll(response.body()?.results!!)
                            trackAdapter.notifyDataSetChanged()//полностью перерисовываем адаптер
                        }
                        if (iTunesTrack.isEmpty()) {
                            setEmptyResponse()
                            trackAdapter.notifyDataSetChanged()//перерисовываем адаптер

                        }
                    }
                }

                @SuppressLint("NotifyDataSetChanged")
                override fun onFailure(call: Call<TracksResponse>, t: Throwable) {
                    iTunesTrack.clear()//при ошибке - очищаем список треков
                    trackAdapter.notifyDataSetChanged()
                    setNoConnectionError()
                }

            })
    }

    private fun onUpdateButtonClickListener() {
        updateButtonClick.setOnClickListener {
            startSearchTrack()
        }

    }
}