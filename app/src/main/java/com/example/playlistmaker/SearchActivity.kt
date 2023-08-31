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
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {
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
        buttonBack = findViewById<ImageButton>(R.id.backToMainActivityViewButtonFromSearch)
        editTextSearch = findViewById<EditText>(R.id.editTextSearchActivity)
        clearButton = findViewById<ImageButton>(R.id.clearEditTextSearchActivity)
        clearButton.setOnClickListener {
            editTextSearch.setText("")
            iTunesTrack.clear()
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
                searchQueryText = editTextSearch.getText().toString()
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
        trackAdapter = TrackAdapter(iTunesTrack)
        val recycleViewTrack = findViewById<RecyclerView>(R.id.track_recycle_view)
        recycleViewTrack.adapter = trackAdapter
        editTextSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (searchQueryText.isNotEmpty()) {
                    val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                    inputMethodManager?.hideSoftInputFromWindow(editTextSearch.windowToken,0)
                    itunesService.search(searchQueryText.toString())
                        .enqueue(object : Callback<TracksResponse> {
                            override fun onResponse(
                                call: Call<TracksResponse>,
                                response: Response<TracksResponse>
                            ) {
                                if (response.code() == 200) {
                                    iTunesTrack.clear()
                                    if (response.body()?.results?.isNotEmpty() == true) {
                                        iTunesTrack.addAll(response.body()?.results!!)
                                        trackAdapter.notifyDataSetChanged()
                                    }
                                    if (iTunesTrack.isEmpty()) {
                                        Toast.makeText(applicationContext, "Ничего нет", Toast.LENGTH_SHORT)
                                    }
                                }
                            }

                            override fun onFailure(call: Call<TracksResponse>, t: Throwable) {
                                Toast.makeText(applicationContext, "Что-то пошло не так - ${t.message.toString()}", Toast.LENGTH_SHORT)                            }

                        })
                }
                true
            } else
            {
                false
            }
        }
    }
}