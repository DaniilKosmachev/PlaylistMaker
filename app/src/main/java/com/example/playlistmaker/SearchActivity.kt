package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
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
import android.widget.LinearLayout.LayoutParams
import android.widget.TextView
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
}

   private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(ITUNES_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val itunesService = retrofit.create(ITunesApi::class.java)

    val iTunesTrack = ArrayList<Track>()//список треков из iTunes

    var searchQueryText = ""

    private lateinit var trackAdapter: TrackAdapter
    private lateinit var editTextSearch: EditText
    private lateinit var buttonBack:ImageButton
    private lateinit var clearButton: ImageButton
    private lateinit var recycleViewTrack: RecyclerView
    private lateinit var updateButtonClick: Button
    private lateinit var errorLayout: LinearLayout
    private lateinit var errorImage: ImageView
    private lateinit var errorText: TextView

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
        trackAdapter = TrackAdapter(iTunesTrack)
        recycleViewTrack.adapter = trackAdapter
    }

    fun initializedViewElementSearchActivity() {
        buttonBack = findViewById(R.id.backToMainActivityViewButtonFromSearch)
        editTextSearch = findViewById(R.id.editTextSearchActivity)
        clearButton = findViewById(R.id.clearEditTextSearchActivity)
        recycleViewTrack = findViewById(R.id.track_recycle_view)
        updateButtonClick = findViewById(R.id.updateQueryButton)
        errorLayout = findViewById(R.id.search_activity_error_linear_layout)
        errorImage = findViewById(R.id.search_activity_error_image)
        errorText = findViewById(R.id.search_activity_error_text)
    }

    private fun setSearchActivityTextWatcher() {
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