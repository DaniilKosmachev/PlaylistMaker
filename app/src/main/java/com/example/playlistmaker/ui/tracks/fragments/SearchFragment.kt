package com.example.playlistmaker.ui.tracks.fragments

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.domain.search.model.Track
import com.example.playlistmaker.ui.audioplayer.activity.AudioPlayerActivity
import com.example.playlistmaker.ui.tracks.TrackAdapter
import com.example.playlistmaker.ui.tracks.model.SearchActivityStatus
import com.example.playlistmaker.ui.tracks.view_model.SearchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment: Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val iTunesTrack = ArrayList<Track>()//список треков из iTunes
    private val iTunesTrackSearchHistory = ArrayList<Track>()//список истории поиска

    var searchQueryText = ""

    private lateinit var trackHistoryAdapter: TrackAdapter
    private lateinit var trackAdapter: TrackAdapter
    private var isClickedAllowed: Boolean? = null //маячек для повторного клика

    private val viewModel by viewModel<SearchViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?  {
        _binding = FragmentSearchBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        clickOnClearButton()
        setSearchFragmentTextWatcher()
        onUpdateButtonClickListener()
        initializeComponents()
        setOnEditTextFocusLisneter()
        clickOnClearSearchHistoryButton()
        observeOnHistorySearchLiveData()
        observeOnSearchLiveData()
        observeOnStatusScreen()
        observeOnIsClickAllowed()
    }

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

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        binding.editTextSearchActivity.setText(searchQueryText)
    }

    fun observeOnHistorySearchLiveData() {
        viewModel.getHistoryListTrack().observe(viewLifecycleOwner) {
            iTunesTrackSearchHistory.clear()
            iTunesTrackSearchHistory.addAll(it)
        }
    }

    fun observeOnSearchLiveData() {
        viewModel.getListTrack().observe(viewLifecycleOwner) {
            if (iTunesTrack.isNotEmpty()) {
                iTunesTrack.clear()
            }
            iTunesTrack.addAll(it)
        }
    }

    fun observeOnStatusScreen() {
        viewModel.getStatusScreen().observe(viewLifecycleOwner) {
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
        viewModel.getIsClickAllowed().observe(viewLifecycleOwner) {
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

    private fun setSearchFragmentTextWatcher() {
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
            binding.searchActivityHistoryTrackLinearLayout.visibility = LinearLayout.GONE
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

    private fun hideSystemKeyboard() {
        val inputMethodManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
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
        val params: LinearLayout.LayoutParams =
            binding.searchActivityErrorLinearLayout.layoutParams as LinearLayout.LayoutParams
        params.setMargins(0, dpToPx((if (isPortrainSystemOrientatin()) 102f else 8f), requireContext()), 0, 0)
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
        Intent(requireContext(), AudioPlayerActivity::class.java).apply {
            putExtra(TrackAdapter.SELECTABLE_TRACK, track)
            startActivity(this)
        }

    }

    companion object {
        private const val SEARCH_STRING = "SEARCH_STRING"
    }
}