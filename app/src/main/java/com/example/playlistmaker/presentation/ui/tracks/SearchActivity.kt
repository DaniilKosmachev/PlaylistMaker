package com.example.playlistmaker.presentation.ui.tracks

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout.GONE
import android.widget.LinearLayout.LayoutParams
import androidx.core.view.isVisible
import com.example.playlistmaker.Creator
import com.example.playlistmaker.presentation.ui.audioplayer.AudioPlayerActivity
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.domain.api.track_history.TrackHistoryInteractor
import com.example.playlistmaker.domain.api.track.TracksInteractor
import com.example.playlistmaker.domain.models.ResponceStatus
import com.example.playlistmaker.domain.models.TrackSearchResponceParams
import com.example.playlistmaker.presentation.TrackAdapter
import java.util.function.Consumer

class SearchActivity : AppCompatActivity(), Consumer<TrackSearchResponceParams>{
    private val iTunesTrack = ArrayList<Track>()//список треков из iTunes
    private val iTunesTrackSearchHistory = ArrayList<Track>()//список истории поиска

    var searchQueryText = ""

    private lateinit var binding: ActivitySearchBinding
    private lateinit var trackHistoryAdapter: TrackAdapter
    private lateinit var trackAdapter: TrackAdapter
    private lateinit var trackInteractor: TracksInteractor
    private lateinit var trackHistoryInteractor: TrackHistoryInteractor
    private var mainTreadHandler: Handler? = null
    private var isClickedAllowed = true //маячек для повторного клика
    private val searchDebounce = Runnable {
        binding.searchActivityProgressBar.isVisible = true
        startSearchTrack()
        hideSystemKeyboard()
    }

    override fun onDestroy() {
        super.onDestroy()
        mainTreadHandler?.removeCallbacks(searchDebounce)
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        trackHistoryInteractor = Creator.provideTrackHistoryInteractor()
        clickOnClearButton()
        clickOnButtonBack()
        setSearchActivityTextWatcher()
        onUpdateButtonClickListener()
        initializeComponents()
        setOnEditTextFocusLisneter()
        readOnHistoryTrackList()
        clickOnClearSearchHistoryButton()
        trackInteractor = Creator.provideTracksInteractor()
    }

    private fun initializeComponents() {
        mainTreadHandler = Handler(Looper.getMainLooper())
        trackAdapter = TrackAdapter(iTunesTrack) {
            if (onClickAllowed()) {
                openAudioPlayerAndReceiveTrackInfo(it)
                trackHistoryInteractor.addNewTrackInTrackHistory(it,iTunesTrackSearchHistory)
                trackHistoryAdapter.notifyDataSetChanged()
            }
        }//адаптер для текущего поискового запроса
        binding.trackRecycleView.adapter =
            trackAdapter//устанавливаем для RecyclerView текущего результата поиска адаптер
        trackHistoryAdapter = TrackAdapter(iTunesTrackSearchHistory) {
            if (onClickAllowed()) {
                openAudioPlayerAndReceiveTrackInfo(it)
                trackHistoryInteractor.updateHistoryListAfterSelectItemHistoryTrack(it)
                iTunesTrackSearchHistory.clear()
                iTunesTrackSearchHistory.addAll(trackHistoryInteractor.getTrackArrayFromShared())
                trackHistoryAdapter.notifyDataSetChanged()
            }
        }//адптер для истории поиска
        binding.searchActivityHistoryRecyclerView.adapter =
            trackHistoryAdapter//устанавливаем для RecyclerView истории поиска адаптер
    }


    private fun onClickAllowed(): Boolean {
        val current = isClickedAllowed
        if (isClickedAllowed) {
            isClickedAllowed = false
            mainTreadHandler?.postDelayed({ isClickedAllowed = true }, CLICK_ON_TRACK_DELAY_MILLIS)
        }
        return current
    }

    fun readOnHistoryTrackList() {//читаем из sheared preferences файла историю поиска
        iTunesTrackSearchHistory.addAll(trackHistoryInteractor.getTrackArrayFromShared())
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
            ) {
                binding.clearEditTextSearchActivity.visibility = clearButtonVisibility(s)
                searchQueryText = binding.editTextSearchActivity.text.toString()
                binding.searchActivityHistoryTrackLinearLayout.visibility =
                    historyLinearLayoutVisibility(s)
                binding.trackRecycleView.visibility = searchRecyclerlViewVisibility(s)
                if (s.isNullOrEmpty()) {
                    binding.searchActivityProgressBar.isVisible = false
                    iTunesTrack.clear()
                    trackAdapter.notifyDataSetChanged()
                }
                if (!s.isNullOrEmpty()) {
                    startDelayMainSearch()
                } else {
                    mainTreadHandler?.removeCallbacks(searchDebounce)
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

            fun searchRecyclerlViewVisibility(s: CharSequence?): Int {
                return if (s.isNullOrEmpty() && iTunesTrackSearchHistory.isNotEmpty()) {
                    View.GONE

                } else {
                    View.VISIBLE
                }
            }
        }
        binding.editTextSearchActivity.addTextChangedListener(searchActivityTextWatcher)
    }

    private fun startDelayMainSearch() {
        binding.searchActivityErrorLinearLayout.isVisible = false
        binding.searchActivityProgressBar.isVisible = true
        mainTreadHandler?.removeCallbacks(searchDebounce)
        mainTreadHandler?.postDelayed(searchDebounce, SEARCH_DELAY_2000_MILLIS)
        if (iTunesTrack.isNotEmpty()) {
            iTunesTrack.clear()
            trackAdapter.notifyDataSetChanged()
        }
    }

    private fun setOnEditTextFocusLisneter() {
        binding.editTextSearchActivity.setOnFocusChangeListener { _, hasFocus ->
            if (iTunesTrackSearchHistory.isNotEmpty()) {
                binding.searchActivityHistoryTrackLinearLayout.visibility =
                    if (hasFocus && binding.editTextSearchActivity.text.isNullOrEmpty()) View.VISIBLE else View.GONE
                trackHistoryAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun clickOnClearSearchHistoryButton() {
        binding.clearSearchHistory.setOnClickListener {
            trackHistoryInteractor.clearSearchHistory()
            iTunesTrackSearchHistory.clear()
            trackHistoryAdapter.notifyDataSetChanged()
            binding.searchActivityHistoryTrackLinearLayout.visibility = GONE
            trackHistoryAdapter.notifyDataSetChanged()
        }
    }

    private fun clickOnClearButton() {
        binding.clearEditTextSearchActivity.setOnClickListener {//кнопка очистки
            binding.searchActivityProgressBar.isVisible = false
            binding.editTextSearchActivity.setText("")//устанавливает текст пустой
            iTunesTrack.clear()//очищаем список треков
            trackAdapter.notifyDataSetChanged()//полностью перерисовываем адаптер
            hideSystemKeyboard()
            binding.trackRecycleView.isVisible = false
            binding.searchActivityErrorLinearLayout.isVisible = false
            trackHistoryAdapter.notifyDataSetChanged()//обновляем историю поиска
        }
    }

    private fun openAudioPlayerAndReceiveTrackInfo(track: Track) {
        Intent(this, AudioPlayerActivity::class.java).apply {
            putExtra(TrackAdapter.SELECTABLE_TRACK, track)
            startActivity(this)
        }

    }

    private fun clickOnButtonBack() {
        binding.backToMainActivityViewButtonFromSearch.setOnClickListener {
            finish()
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
        binding.searchActivityProgressBar.isVisible = false
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
        binding.searchActivityProgressBar.isVisible = false
        binding.trackRecycleView.isVisible = false
        binding.searchActivityErrorLinearLayout.isVisible = true
        updateErrorlayoutParamsForLandscapeOrientation()
        if (!isPortrainSystemOrientatin()) {//если не портретная ориентация, то заменяю все знаки новой строки, чтобы влезло в книжную ориентацию
            binding.searchActivityErrorText.text =
                getString(R.string.connection_error).replace("\n", " ")
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

    fun startSearchTrack() {
        if (!binding.editTextSearchActivity.text.isNullOrEmpty()) {
                trackInteractor.searchTracks(binding.editTextSearchActivity.text.toString(), this)
        }
    }


    private fun onUpdateButtonClickListener() {
        binding.updateQueryButton.setOnClickListener {
            binding.searchActivityErrorLinearLayout.isVisible = false
            binding.searchActivityProgressBar.isVisible = true
            startSearchTrack()
        }

    }

    override fun accept(t: TrackSearchResponceParams) {
        runOnUiThread {
            iTunesTrack.clear()
            if (t.tracks.isNotEmpty() && t.resultResponse == ResponceStatus.OK) {
                iTunesTrack.clear()
                iTunesTrack.addAll(t.tracks)
                binding.searchActivityProgressBar.isVisible = false
                trackAdapter.notifyDataSetChanged()
            } else if (t.tracks.isEmpty() && t.resultResponse == ResponceStatus.OK) {
                iTunesTrack.clear()
                trackAdapter.notifyDataSetChanged()
                binding.searchActivityProgressBar.isVisible = false
                setEmptyResponse()
            } else if (t.tracks.isEmpty() && t.resultResponse == ResponceStatus.BAD) {
                iTunesTrack.clear()
                trackAdapter.notifyDataSetChanged()
                binding.searchActivityProgressBar.isVisible = false
                setNoConnectionError()
            }
        }
    }

    companion object {
        private const val CLICK_ON_TRACK_DELAY_MILLIS = 1000L
        private const val SEARCH_DELAY_2000_MILLIS = 2000L
        private const val SEARCH_STRING = "SEARCH_STRING"
    }
}
