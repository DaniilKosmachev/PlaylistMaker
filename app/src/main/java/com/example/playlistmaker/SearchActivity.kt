package com.example.playlistmaker

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
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity(), TrackAdapter.OnUpdateButtonClickListener {
    private val iTunesBaseUrl = "https://itunes.apple.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val itunesService = retrofit.create(ITunesApi::class.java)

    val iTunesTrack = ArrayList<Track>()

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        buttonBack = findViewById(R.id.backToMainActivityViewButtonFromSearch)
        editTextSearch = findViewById(R.id.editTextSearchActivity)
        clearButton = findViewById(R.id.clearEditTextSearchActivity)
        clearButton.setOnClickListener {
            editTextSearch.setText("")
            iTunesTrack.clear()
            trackAdapter.items.clear()
            trackAdapter.notifyDataSetChanged()
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
                    startSearchTrack()
                }
                true
            } else
            {
                false
            }
        }
    }
    fun startSearchTrack() {
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(editTextSearch.windowToken,0)
        itunesService.search(searchQueryText)
            .enqueue(object : Callback<TracksResponse> {
                override fun onResponse(
                    call: Call<TracksResponse>,
                    response: Response<TracksResponse>
                ) {
                    if (response.code() == 200) {
                        trackAdapter.items.clear()
                        iTunesTrack.clear()
                        if (response.body()?.results?.isNotEmpty() == true) {
                            iTunesTrack.addAll(response.body()?.results!!)
                            trackAdapter.items.addAll(iTunesTrack)
                            trackAdapter.notifyDataSetChanged()
                        }
                        if (iTunesTrack.isEmpty()) {
                            trackAdapter.items.clear()
                            trackAdapter.items.add(EmptySearchPlaceHolder())
                            trackAdapter.notifyDataSetChanged()
                        }
                    }
                }

                override fun onFailure(call: Call<TracksResponse>, t: Throwable) {
                    iTunesTrack.clear()
                    trackAdapter.items.clear()
                    trackAdapter.items.add(NoConnectionPlaceHolder())
                    trackAdapter.notifyDataSetChanged()
                }

            })
    }
       override fun onUpdateButtonClickListener() {
           startSearchTrack()
    }
}