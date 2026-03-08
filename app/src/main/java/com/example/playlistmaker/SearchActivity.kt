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
import androidx.core.view.isVisible
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
    private val tracksRetrofit = Retrofit.Builder()
        .baseUrl(TRACKS_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val tracksService = tracksRetrofit.create(SearchTracksApi::class.java)
    private val tracks = mutableListOf<Track>()
    private val tracksAdapter = SearchTracksAdapter{onItemClick(tracks[it])}
    private val historyTracksAdapter = SearchTracksAdapter{}

    private var textSearch: String = TEXT_SEARCH_VALUE
    private var lastTextSearch: String = ""

    private lateinit var messagePlaceholder: LinearLayout
    private lateinit var messageImage: ImageView
    private lateinit var messageText: TextView
    private lateinit var messageButton: Button
    private lateinit var inputTextSearch: EditText
    private lateinit var llSearchHistory: LinearLayout
    private lateinit var buttonClearHistory: Button
    private lateinit var searchHistory: SearchHistory

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
        llSearchHistory = findViewById(R.id.ll_search_history)
        buttonClearHistory = findViewById(R.id.btn_clear_history)

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
            tracks.clear()
            tracksAdapter.notifyDataSetChanged()
            if (messagePlaceholder.isVisible){
                showMessage("","")
            }
            if (searchHistory.historyTracks.isNotEmpty()){
                llSearchHistory.isVisible = true
            }
        }
        inputTextSearch.doOnTextChanged { text, start, before, count ->
            buttonClearSearch.isVisible = buttonClearSearchVisibility(text)
            textSearch = text.toString()

            llSearchHistory.isVisible = (inputTextSearch.hasFocus()
                    && text?.isEmpty() == true
                    && searchHistory.historyTracks.isNotEmpty()
                    && !(messagePlaceholder.isVisible))

            if (text?.isEmpty() == true){
                tracks.clear()
                tracksAdapter.notifyDataSetChanged()
                historyTracksAdapter.tracks = searchHistory.historyTracks
                historyTracksAdapter.notifyDataSetChanged()
            }
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
        inputTextSearch.setOnFocusChangeListener { view, hasFocus ->
            llSearchHistory.isVisible = (hasFocus
                    && inputTextSearch.text.isEmpty()
                    && searchHistory.historyTracks.isNotEmpty()
                    && !(messagePlaceholder.isVisible))
        }
        messageButton.setOnClickListener {
            searchTracks(lastTextSearch)
            inputTextSearch.setText(lastTextSearch)
        }

        val sharedPrefs = getSharedPreferences(SEARCH_PREFERENCES, MODE_PRIVATE)
        searchHistory = SearchHistory(sharedPrefs)

        val rvHistoryTracks = findViewById<RecyclerView>(R.id.history_tracks)
        rvHistoryTracks.layoutManager = LinearLayoutManager(this)
        rvHistoryTracks.adapter = historyTracksAdapter
        historyTracksAdapter.tracks = searchHistory.historyTracks

        buttonClearHistory.setOnClickListener {
            searchHistory.clearHistory()
            historyTracksAdapter.tracks = searchHistory.historyTracks
            historyTracksAdapter.notifyDataSetChanged()
            llSearchHistory.isVisible = false
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
                    if (response.isSuccessful){
                        val results = response.body()?.results
                        if (results?.isNotEmpty() == true){
                            tracks.clear()
                            tracks.addAll(results)
                            tracksAdapter.notifyDataSetChanged()
                            showMessage("", "")
                        }else{
                            showMessage(getString(R.string.nothing_was_found), "")
                        }
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
            messagePlaceholder.isVisible = true
            tracks.clear()
            tracksAdapter.notifyDataSetChanged()
            messageText.text = text
            if (additionalMessage.isEmpty()){
                messageButton.isVisible = false
                messageImage.setImageResource(R.drawable.ic_nothing_was_found_120)
            }else{
                messageButton.isVisible = true
                inputTextSearch.setText("")
                messageImage.setImageResource(R.drawable.ic_communication_problems_120)
            }
            llSearchHistory.isVisible = false
        }else{
            messagePlaceholder.isVisible = false
            messageButton.isVisible = false
        }
    }

    private fun buttonClearSearchVisibility(s: CharSequence?): Boolean {
        return !s.isNullOrEmpty()
    }

    private fun onItemClick(track: Track){
        searchHistory.saveTrack(track)
    }

    companion object {
        const val TEXT_SEARCH_KEY = "TEXT_SEARCH"
        const val TEXT_SEARCH_VALUE = ""
        const val TRACKS_BASE_URL = "https://itunes.apple.com"
        const val SEARCH_PREFERENCES = "search_preferences"
    }
}
