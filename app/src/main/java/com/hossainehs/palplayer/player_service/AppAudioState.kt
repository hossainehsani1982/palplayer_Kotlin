package com.hossainehs.palplayer.player_service

sealed class AppAudioState {
    data object Initializing : AppAudioState()
    data class Ready(val duration: Long) : AppAudioState()
    data class Progress(val progress: Long) : AppAudioState()
    data class Buffering(val progress: Long) : AppAudioState()
    data class Playing(val isPlaying : Boolean) : AppAudioState()
    data class CurrentlyPlaying(val mediaItemIndex : Int) : AppAudioState()
}