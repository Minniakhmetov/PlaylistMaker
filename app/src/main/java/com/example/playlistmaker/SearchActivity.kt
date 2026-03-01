package com.example.playlistmaker

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {
    private val tracksBaseUrl = "https://itunes.apple.com"
    private val tracksRetrofit = Retrofit.Builder()
        .baseUrl(tracksBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val tracksService = tracksRetrofit.create(SearchTracksApi::class.java)
    private val tracks = mutableListOf<Track>()
    private val tracksAdapter = SearchTracksAdapter()

    private var textSearch: String = TEXT_SEARCH_VALUE
    private var lastTextSearch: String = ""

    private lateinit var messagePlaceholder: LinearLayout
    private lateinit var messageImage: ImageView
    private lateinit var messageText: TextView
    private lateinit var messageButton: Button
    private lateinit var inputTextSearch: EditText

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
        messagePlaceholder = findViewById(R.id.message_placeholder)
        messageImage = findViewById(R.id.message_image)
        messageText = findViewById(R.id.message_text)
        messageButton = findViewById(R.id.message_button)

        val buttonBack = findViewById<MaterialToolbar>(R.id.toolbar_search)
        buttonBack.setNavigationOnClickListener {
            finish()
        }
        inputTextSearch = findViewById(R.id.input_text_search)
        if (savedInstanceState != null) {
            textSearch = savedInstanceState.getString(TEXT_SEARCH_KEY, TEXT_SEARCH_VALUE)
            if (textSearch.isNotEmpty()){
                inputTextSearch.setText(textSearch)
            }
        }
        val buttonClearSearch = findViewById<ImageView>(R.id.button_clear_search)
        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
        buttonClearSearch.setOnClickListener {
            inputTextSearch.setText("")
            inputMethodManager?.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)

        }
        inputTextSearch.doOnTextChanged { text, start, before, count ->
            buttonClearSearch.visibility = buttonClearSearchVisibility(text)
            textSearch = text.toString()
        }

        val rvSearchTracks = findViewById<RecyclerView>(R.id.tracksList)
        rvSearchTracks.layoutManager = LinearLayoutManager(this)
        rvSearchTracks.adapter = tracksAdapter
        tracksAdapter.tracks = tracks

        inputTextSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE){
                if (textSearch.isNotEmpty()){
                    searchTracks(textSearch)
                    lastTextSearch = textSearch
                }
                true
            }
            false
        }
        messageButton.setOnClickListener {
            searchTracks(lastTextSearch)
            inputTextSearch.setText(lastTextSearch)
        }
    }

    private fun searchTracks(searchTrack: String){
        tracksService
            .getTracks(searchTrack)
            .enqueue(object : Callback<SearchTracksResponse>{
                override fun onResponse(
                    call: Call<SearchTracksResponse?>,
                    response: Response<SearchTracksResponse?>,
                ) {
                    if (response.body()?.results?.isNotEmpty() == true){
                        tracks.clear()
                        tracks.addAll(response.body()?.results!!)
                        tracksAdapter.notifyDataSetChanged()
                        showMessage("", "")
                    }else{
                        showMessage(getString(R.string.nothing_was_found), "")
                    }
                }
                override fun onFailure(
                    call: Call<SearchTracksResponse?>,
                    t: Throwable,
                ) {
                    showMessage(getString(R.string.communication_problems), t.message.toString())
                }
            })
    }

    private fun showMessage(text: String, additionalMessage: String){
        if (text.isNotEmpty()) {
            messagePlaceholder.visibility = View.VISIBLE
            tracks.clear()
            tracksAdapter.notifyDataSetChanged()
            messageText.text = text
            if (additionalMessage.isEmpty()){
                messageButton.visibility = View.GONE
                messageImage.setImageResource(R.drawable.ic_nothing_was_found_120)

            }else{
                messageButton.visibility = View.VISIBLE
                inputTextSearch.setText("")
                messageImage.setImageResource(R.drawable.ic_communication_problems_120)
            }
        }else{
            messagePlaceholder.visibility = View.GONE
            messageButton.visibility = View.GONE
        }
    }

    private fun buttonClearSearchVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            tracks.clear()
            tracksAdapter.notifyDataSetChanged()
            showMessage("","")
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
