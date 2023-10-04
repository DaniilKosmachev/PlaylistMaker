package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputBinding
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
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.google.gson.Gson
import retrofit2.Retrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(ITUNES_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val itunesService = retrofit.create(ITunesApi::class.java)

    private val iTunesTrack = ArrayList<Track>()//список треков из iTunes
    private val iTunesTrackSearchHistory = ArrayList<Track>()//список истории поиска

    var searchQueryText = ""

    private lateinit var binding: ActivitySearchBinding
    private lateinit var trackHistoryAdapter: TrackAdapter
    private lateinit var trackAdapter: TrackAdapter
    private lateinit var historySharedPreferences: SharedPreferences
    private lateinit var classHistorySearch: TrackSearchHistory

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
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        clickOnClearButton()
        clickOnButtonBack()
        setSearchActivityTextWatcher()
        clickOnButtonDoneSystemKeyboard()
        onUpdateButtonClickListener()
        initializeComponents()
        setOnEditTextFocusLisneter()
        readOnHistoryTrackList()
        clickOnClearSearchHistoryButton()
    }

    private fun initializeComponents() {
        historySharedPreferences = getSharedPreferences(
            SHARED_PREFERENCES_HISTORY_SEARCH_FILE_NAME,
            MODE_PRIVATE
        )//инициализируем экземпляр SP
        classHistorySearch = TrackSearchHistory(historySharedPreferences)
        trackAdapter = TrackAdapter(iTunesTrack) {
            openAudioPlayerAndReceiveTrackInfo(it)
            classHistorySearch.addNewTrackInTrackHistory(it, iTunesTrackSearchHistory)
            trackHistoryAdapter.notifyDataSetChanged()
        }//адаптер для текущего поискового запроса
        binding.trackRecycleView.adapter =
            trackAdapter//устанавливаем для RecyclerView текущего результата поиска адаптер
        trackHistoryAdapter = TrackAdapter(iTunesTrackSearchHistory) {
            openAudioPlayerAndReceiveTrackInfo(it)
            classHistorySearch.updateHistoryListAfterSelectItemHistoryTrack(it)
            iTunesTrackSearchHistory.clear()
            iTunesTrackSearchHistory.addAll(classHistorySearch.getTrackArrayFromShared())
            trackHistoryAdapter.notifyDataSetChanged()
        }//адптер для истории поиска
        binding.searchActivityHistoryRecyclerView.adapter =
            trackHistoryAdapter//устанавливаем для RecyclerView истории поиска адаптер
    }

    @SuppressLint("NotifyDataSetChanged")
    fun readOnHistoryTrackList() {//читаем из sheared preferences файла историю поиска
        iTunesTrackSearchHistory.addAll(classHistorySearch.getTrackArrayFromShared())
        trackHistoryAdapter.notifyDataSetChanged()
    }


    private fun setSearchActivityTextWatcher() {
        val searchActivityTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {    //Видно основной поиск!
                binding.clearEditTextSearchActivity.visibility = clearButtonVisibility(s)
                searchQueryText = binding.editTextSearchActivity.text.toString()
                binding.searchActivityHistoryTrackLinearLayout.visibility = historyLinearLayoutVisibility(s)
                binding.trackRecycleView.visibility = searchRecyclerlViewVisibility(s)
                if (s.isNullOrEmpty()) {
                    iTunesTrack.clear()
                    trackAdapter.notifyDataSetChanged()
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

            fun clearButtonVisibility(s: CharSequence?): Int {
                return if (s.isNullOrEmpty()) {
                    View.GONE
                } else {
                    View.VISIBLE
                }
            }

            fun historyLinearLayoutVisibility(s: CharSequence?): Int {
                return if (s.isNullOrEmpty() && iTunesTrackSearchHistory.isNotEmpty()) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
            }

            fun searchRecyclerlViewVisibility(s: CharSequence?): Int {  //возможно поправит баг, когда не скрывается поиск при удалении символов при пустой строке
                return if (s.isNullOrEmpty() && iTunesTrackSearchHistory.isNotEmpty()) {
                    View.GONE

                } else {
                    View.VISIBLE
                }
            }
        }
        binding.editTextSearchActivity.addTextChangedListener(searchActivityTextWatcher)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setOnEditTextFocusLisneter() {
        binding.editTextSearchActivity.setOnFocusChangeListener { _, hasFocus ->
            if (iTunesTrackSearchHistory.isNotEmpty()) {
                binding.searchActivityHistoryTrackLinearLayout.visibility =
                    if (hasFocus && binding.editTextSearchActivity.text.isNullOrEmpty()) View.VISIBLE else View.GONE
                trackHistoryAdapter.notifyDataSetChanged()
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun clickOnClearSearchHistoryButton() {
        binding.clearSearchHistory.setOnClickListener {
            classHistorySearch.clearSearchHistory()
            iTunesTrackSearchHistory.clear()
            trackHistoryAdapter.notifyDataSetChanged()
            binding.searchActivityHistoryTrackLinearLayout.visibility = GONE
            trackHistoryAdapter.notifyDataSetChanged()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun clickOnClearButton() {
        binding.clearEditTextSearchActivity.setOnClickListener {//кнопка очистки
            binding.editTextSearchActivity.setText("")//устанавливает текст пустой
            iTunesTrack.clear()//очищаем список треков
            trackAdapter.notifyDataSetChanged()//полностью перерисовываем адаптер
            hideSystemKeyboard()
            binding.trackRecycleView.isVisible = false
            binding.searchActivityErrorLinearLayout.isVisible = false
            trackHistoryAdapter.notifyDataSetChanged()//обновляем историю поиска
        }
    }

    fun openAudioPlayerAndReceiveTrackInfo(track: Track) {
        Intent(this, AudioPlayerActivity::class.java).apply {
            putExtra(TrackAdapter.SELECTABLE_TRACK,track)
            startActivity(this)
        }

    }

    private fun clickOnButtonBack() {
        binding.backToMainActivityViewButtonFromSearch.setOnClickListener {
            finish()
        }
    }

    private fun clickOnButtonDoneSystemKeyboard() {
        binding.editTextSearchActivity.setOnEditorActionListener { _, actionId, _ ->
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
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(binding.editTextSearchActivity.windowToken, 0)
    }

    private fun isPortrainSystemOrientatin(): Boolean {//true - если портретная
        return resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
    }

    private fun isNightModeOn(): Boolean {//если дневная - false, ночная - true
        return when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> {
                true
            }
            Configuration.UI_MODE_NIGHT_NO -> {
                false
            }
            Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                true
            }
            else -> true
        }
    }

    private fun setEmptyResponse() { //устанавливает в ErrorLinearLayout картинку и текст по ошибке - пустой ответ
        binding.trackRecycleView.isVisible = false
        binding.searchActivityErrorLinearLayout.isVisible = true
        updateErrorlayoutParamsForLandscapeOrientation()
        binding.searchActivityErrorText.text = getString(R.string.empty_search)
        if (isNightModeOn()) {
            binding.searchActivityErrorImage.setImageResource(R.drawable.emptyseacrhresultdark)
        } else {
            binding.searchActivityErrorImage.setImageResource(R.drawable.emptysearchresultlight)
        }
        binding.updateQueryButton.isVisible = false
    }

    private fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics
        ).toInt()
    }

    private fun updateErrorlayoutParamsForLandscapeOrientation() {
        val params: LayoutParams =
            binding.searchActivityErrorLinearLayout.layoutParams as LayoutParams
        params.setMargins(0, dpToPx((if (isPortrainSystemOrientatin()) 102f else 8f), this), 0, 0)
    }

    private fun setNoConnectionError() { //устанавливает в ErrorLinearLayout картинку и текст по ошибке - отсутствует связь
        binding.trackRecycleView.isVisible = false
        binding.searchActivityErrorLinearLayout.isVisible = true
        updateErrorlayoutParamsForLandscapeOrientation()
        if (!isPortrainSystemOrientatin()) {//если не портретная ориентация, то заменяю все знаки новой строки, чтобы влезло в книжную ориентацию
            binding.searchActivityErrorText.text = getString(R.string.connection_error).replace("\n", " ")
        } else {
            binding.searchActivityErrorText.text = getString(R.string.connection_error)
        }
        if (isNightModeOn()) {
            binding.searchActivityErrorImage.setImageResource(R.drawable.inteneterrordark)
        } else {
            binding.searchActivityErrorImage.setImageResource(R.drawable.interneterrorlight)
        }
        binding.updateQueryButton.isVisible = true
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
                            binding.searchActivityErrorLinearLayout.isVisible = false
                            binding.trackRecycleView.isVisible = true
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
        binding.updateQueryButton.setOnClickListener {
            startSearchTrack()
        }

    }

    companion object {
        private const val SEARCH_STRING = "SEARCH_STRING"
        const val ITUNES_BASE_URL = "https://itunes.apple.com"
        const val SHARED_PREFERENCES_HISTORY_SEARCH_FILE_NAME = "history_search_track"
    }

}