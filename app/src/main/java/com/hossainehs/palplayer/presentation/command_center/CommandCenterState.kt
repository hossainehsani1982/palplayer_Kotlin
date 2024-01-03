package com.hossainehs.palplayer.presentation.command_center

import androidx.lifecycle.SavedStateHandle
import com.hossainehs.palplayer.domain.model.MediaFile
import kotlinx.coroutines.flow.Flow

class CommandCenterState (
    private val savedStateHandle: SavedStateHandle
) {

    var isPlaying = savedStateHandle.get<Boolean>("isPlaying") ?: false
        private set(value) {
            field = value
            savedStateHandle["isPlaying"] = value
        }
    fun updateIsPlaying(isPlaying: Boolean) {
        this.isPlaying = isPlaying
    }

    private val dummyFile = MediaFile(
        audioFileId = 0,
        path = "",
        displayName = "",
        artist = "",
        duration = 0,
    )
    var currentPlayingFile = savedStateHandle.getLiveData(
        "currentPlayingFile",
        dummyFile)
        private set(value) {
            field = value
            savedStateHandle["currentPlayingFile"] = value
        }
    fun updateCurrentPlayingFile(currentPlayingFile: MediaFile) {
        this.currentPlayingFile.value = currentPlayingFile
    }


    var progressString = savedStateHandle.getLiveData<String>("progressString")
        private set(value) {
            field = value
            savedStateHandle["progressString"] = value
        }
    fun updateProgressString(progressString: String) {
        this.progressString.value = progressString
    }




}