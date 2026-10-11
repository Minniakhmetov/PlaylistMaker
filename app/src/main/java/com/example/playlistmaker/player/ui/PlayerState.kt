package com.example.playlistmaker.player.ui

sealed class PlayerState(val progress: Int) {
    class Default : PlayerState(0)
    class Prepared : PlayerState(0)
    class Playing(progress: Int) : PlayerState(progress)
    class Paused(progress: Int) : PlayerState(progress)
}
