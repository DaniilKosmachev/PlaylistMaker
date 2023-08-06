package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton

class SearchActivity : AppCompatActivity() {
    companion object {
        var searchQueryText = ""
        const val SEARCH_STRING = "SEARCH_STRING"
    }
    override fun onSaveInstanceState(outState: Bundle) {
        val editTextSearch = findViewById<EditText>(R.id.editTextSearchActivity)
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
        val buttonBack = findViewById<ImageButton>(R.id.backToMainActivityViewButtonFromSearch)
        val editTextSearch = findViewById<EditText>(R.id.editTextSearchActivity)
        val clearButton = findViewById<ImageButton>(R.id.clearEditTextSearchActivity)
        clearButton.setOnClickListener {
            editTextSearch.setText("")
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
    }
}