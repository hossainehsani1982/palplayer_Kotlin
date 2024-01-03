package com.hossainehs.palplayer.player_service

sealed class AppPlayerEvents {
    data object PlayPause : AppPlayerEvents()
    data object SelectedAudioChanged : AppPlayerEvents()
    data object SeekToNext : AppPlayerEvents()
    data object SeekToPrevious : AppPlayerEvents()
    data object Backward : AppPlayerEvents()
    data object SeekTo : AppPlayerEvents()
    data object Forward : AppPlayerEvents()
    data object Stop : AppPlayerEvents()
    data class UpdateProgress(val newProgress : Float) : AppPlayerEvents()
}