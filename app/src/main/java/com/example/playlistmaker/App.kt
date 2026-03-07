package com.example.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit

class App : Application() {
    lateinit var sharedPrefs: SharedPreferences

    fun getThemeSetting(): Boolean{
        return sharedPrefs.getBoolean(SETTING_THEME_KEY, false)
    }

    override fun onCreate() {
        super.onCreate()
        sharedPrefs = getSharedPreferences(SETTING_PREFERENCES, MODE_PRIVATE)
        AppCompatDelegate.setDefaultNightMode(
            if (sharedPrefs.getBoolean(SETTING_THEME_KEY, false)){
                AppCompatDelegate.MODE_NIGHT_YES
            }else{
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    fun switchTheme(darkThemeEnabled: Boolean){
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled){
                AppCompatDelegate.MODE_NIGHT_YES
            }else{
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
        saveTheme(darkThemeEnabled)
    }

    fun saveTheme(checked: Boolean){
        sharedPrefs.edit {
            putBoolean(SETTING_THEME_KEY, checked)
        }
    }

    companion object {
        const val SETTING_PREFERENCES = "setting_preferences"
        const val SETTING_THEME_KEY = "key_for_setting_theme"
    }
}
