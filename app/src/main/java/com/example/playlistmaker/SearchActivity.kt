package com.example.playlistmaker

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doOnTextChanged
import com.google.android.material.appbar.MaterialToolbar

class SearchActivity : AppCompatActivity() {

    private var textSearch: String = TEXT_SEARCH_VALUE

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(TEXT_SEARCH_KEY, textSearch)
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
        buttonClearSearch.setOnClickListener {
            inputTextSearch.setText("")
            inputMethodManager?.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)

        }
        inputTextSearch.doOnTextChanged { text, start, before, count ->
            buttonClearSearch.visibility = buttonClearSearchVisibility(text)
            textSearch = text.toString()
        }
    }

    private fun buttonClearSearchVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }
    companion object {
        const val TEXT_SEARCH_KEY = "TEXT_SEARCH"
        const val TEXT_SEARCH_VALUE = ""
    }
}
