package com.example.playlistmaker

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.appbar.MaterialToolbar

class SearchActivity : AppCompatActivity() {

    private var textSearch: String = TEXT_SEARCH_VALUE

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(TEXT_SEARCH_KEY, textSearch)
    }
    companion object {
        const val TEXT_SEARCH_KEY = "TEXT_SEARCH"
        const val TEXT_SEARCH_VALUE = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.search_activity)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val buttonBack = findViewById<MaterialToolbar>(R.id.toolbar_search)
        buttonBack.setNavigationOnClickListener {
            finish()
        }
        val inputTextSearch = findViewById<EditText>(R.id.input_text_search)
        if (savedInstanceState != null) {
            textSearch = savedInstanceState.getString(TEXT_SEARCH_KEY, TEXT_SEARCH_VALUE)
            if (textSearch.isNotEmpty()){
                inputTextSearch.setText(textSearch)
            }
        }
        val buttonClearSearch = findViewById<ImageView>(R.id.button_clear_search)
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        //val view = this.currentFocus
        buttonClearSearch.setOnClickListener {
            inputTextSearch.setText("")
            inputMethodManager?.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)

        }
        val searchTextWatcher = object : TextWatcher{
            override fun beforeTextChanged(
                s: CharSequence?,
                p1: Int,
                p2: Int,
                p3: Int
            ) {
                //
            }
            override fun onTextChanged(
                s: CharSequence?,
                p1: Int,
                p2: Int,
                p3: Int
            ) {
                buttonClearSearch.visibility = buttonClearSearchVisibility(s)

            }
            override fun afterTextChanged(s: Editable?) {
                textSearch = s.toString()
            }
        }
        inputTextSearch.addTextChangedListener(searchTextWatcher)
    }

    private fun buttonClearSearchVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }
}
