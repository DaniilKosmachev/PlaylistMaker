package com.example.playlistmaker.ui.tracks.activity

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout.GONE
import android.widget.LinearLayout.LayoutParams
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.domain.search.model.Track
import com.example.playlistmaker.ui.audioplayer.activity.AudioPlayerActivity
import com.example.playlistmaker.ui.tracks.TrackAdapter
import com.example.playlistmaker.ui.tracks.model.SearchActivityStatus
import com.example.playlistmaker.ui.tracks.view_model.SearchActivityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchActivity : AppCompatActivity() {

    private val viewModel by viewModel<SearchActivityViewModel>()

    private val iTunesTrack = ArrayList<Track>()//список треков из iTunes
    private val iTunesTrackSearchHistory = ArrayList<Track>()//список истории поиска

    var searchQueryText = ""

    private lateinit var binding: ActivitySearchBinding

    private lateinit var trackHistoryAdapter: TrackAdapter
    private lateinit var trackAdapter: TrackAdapter
    private var isClickedAllowed: Boolean? = null //маячек для повторного клика


    override fun onDestroy() {
        super.onDestroy()
        viewModel.removeCallbackSearch()
    }

    override fun onResume() {
        super.onResume()
        trackHistoryAdapter.notifyDataSetChanged()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_STRING, searchQueryText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        binding.editTextSearchActivity.setText(searchQueryText)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        clickOnClearButton()
        clickOnButtonBack()
        setSearchActivityTextWatcher()
        onUpdateButtonClickListener()
        initializeComponents()
        setOnEditTextFocusLisneter()
        clickOnClearSearchHistoryButton()
        observeOnHistorySearchLiveData()
        observeOnSearchLiveData()
        observeOnStatusScreen()
        observeOnIsClickAllowed()
    }

    fun observeOnHistorySearchLiveData() {
        viewModel.getHistoryListTrack().observe(this) {
            iTunesTrackSearchHistory.clear()
            iTunesTrackSearchHistory.addAll(it)
        }
    }

    fun observeOnSearchLiveData() {
        viewModel.getListTrack().observe(this) {
            if (iTunesTrack.isNotEmpty()) {
                iTunesTrack.clear()
            }
            iTunesTrack.addAll(it)
        }
    }

    fun observeOnStatusScreen() {
        viewModel.getStatusScreen().observe(this) {
            when (it) {
                is SearchActivityStatus.Data -> {
                    iTunesTrack.clear()
                    iTunesTrack.addAll(it.data)
                    binding.searchActivityProgressBar.isVisible = false
                    binding.searchActivityHistoryTrackLinearLayout.isVisible = false
                    binding.trackRecycleView.isVisible = true
                    hideSystemKeyboard()
                    trackAdapter.notifyDataSetChanged()
                }
                is SearchActivityStatus.EmptyData -> {
                    iTunesTrack.clear()
                    trackAdapter.notifyDataSetChanged()
                    binding.searchActivityProgressBar.isVisible = false
                    hideSystemKeyboard()
                    setEmptyResponse()
                }
                is SearchActivityStatus.ErrorConnection -> {
                    iTunesTrack.clear()
                    trackAdapter.notifyDataSetChanged()
                    binding.searchActivityProgressBar.isVisible = false
                    hideSystemKeyboard()
                    setNoConnectionError()
                }
                is SearchActivityStatus.Loading -> {
                    iTunesTrack.clear()
                    trackAdapter.notifyDataSetChanged()
                    binding.searchActivityErrorLinearLayout.isVisible = false
                    binding.searchActivityProgressBar.isVisible = true
                    binding.searchActivityHistoryRecyclerView.isVisible = false
                }
                is SearchActivityStatus.ShowHistory -> {
                    if (iTunesTrackSearchHistory.isNotEmpty()) {
                        binding.searchActivityErrorLinearLayout.isVisible = false
                        binding.searchActivityProgressBar.isVisible = false
                        binding.searchActivityHistoryTrackLinearLayout.isVisible = true
                        iTunesTrack.clear()
                        trackAdapter.notifyDataSetChanged()
                        trackHistoryAdapter.notifyDataSetChanged()
                        binding.searchActivityHistoryRecyclerView.isVisible = true
                    }
                }
                else -> {

                }
            }
        }
    }

    fun observeOnIsClickAllowed() {
        viewModel.getIsClickAllowed().observe(this) {
            isClickedAllowed = it
        }
    }

    private fun initializeComponents() {
        trackAdapter = TrackAdapter(iTunesTrack) {
            if (isClickedAllowed!!) {
                openAudioPlayerAndReceiveTrackInfo(it)
                viewModel.addNewTrackInTrackHistory(it)
                trackHistoryAdapter.notifyDataSetChanged()
            }
        }//адаптер для текущего поискового запроса
        binding.trackRecycleView.adapter = trackAdapter//устанавливаем для RecyclerView текущего результата поиска адаптер
        trackHistoryAdapter = TrackAdapter(iTunesTrackSearchHistory) {
            if (isClickedAllowed!!) {
                openAudioPlayerAndReceiveTrackInfo(it)
                viewModel.updateHistoryListAfterSelectItemHistoryTrack(it)
            }
        }//адптер для истории поиска
        binding.searchActivityHistoryRecyclerView.adapter = trackHistoryAdapter//устанавливаем для RecyclerView истории поиска адаптер
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
                viewModel.searchQuery = searchQueryText
                if (s.isNullOrEmpty()) {
                    binding.searchActivityProgressBar.isVisible = false
                    iTunesTrack.clear()
                    trackAdapter.notifyDataSetChanged()
                    viewModel.removeCallbackSearch()
                }
                else {
                    viewModel.startDelaySearch()
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
            viewModel.clearSearchHistory()
            trackHistoryAdapter.notifyDataSetChanged()
            binding.searchActivityHistoryTrackLinearLayout.visibility = GONE
        }
    }

    private fun clickOnClearButton() {
        binding.clearEditTextSearchActivity.setOnClickListener {
            viewModel.removeCallbackSearch()//кнопка очистки
            binding.editTextSearchActivity.text.clear()//устанавливает текст пустой //полностью перерисовываем адаптер
            hideSystemKeyboard()
            binding.searchActivityHistoryRecyclerView.isVisible = true
            binding.trackRecycleView.isVisible = false
            binding.searchActivityErrorLinearLayout.isVisible = false //обновляем историю поиска
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
        return when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) { //ВОЗМОЖНО ВО ВЬЮМОДЕЛЬ!
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
    }

    private fun onUpdateButtonClickListener() {
        binding.updateQueryButton.setOnClickListener {
            binding.searchActivityErrorLinearLayout.isVisible = false
            viewModel.searchQuery = searchQueryText
            iTunesTrack.clear()
            viewModel.startDelaySearch()
        }

    }

    private fun openAudioPlayerAndReceiveTrackInfo(track: Track) {
        Intent(this, AudioPlayerActivity::class.java).apply {
            putExtra(TrackAdapter.SELECTABLE_TRACK, track)
            startActivity(this)
        }

    }

    companion object {
        private const val SEARCH_STRING = "SEARCH_STRING"
    }
}
