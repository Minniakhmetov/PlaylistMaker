package com.example.playlistmaker

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonSearch = findViewById<Button>(R.id.search)
        val searchClickListener: View.OnClickListener = object : View.OnClickListener{
            override fun onClick(v: View?) {
                val displaySearch = Intent(this@MainActivity, SearchActivity::class.java)
                startActivity(displaySearch)
            }
        }
        buttonSearch.setOnClickListener (searchClickListener)

        val buttonMedicalLibrary = findViewById<Button>(R.id.medical_library)
        buttonMedicalLibrary.setOnClickListener {
            val displayMedicalLibrary = Intent(this@MainActivity, MedicalLibraryActivity::class.java)
            startActivity(displayMedicalLibrary)
        }


        val buttonSettings = findViewById<Button>(R.id.settings)
        buttonSettings.setOnClickListener {
            val displaySettings = Intent(this@MainActivity, SettingsActivity::class.java)
            startActivity(displaySettings)
        }
    }
}
