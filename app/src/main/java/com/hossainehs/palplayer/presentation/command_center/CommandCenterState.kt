package com.hossainehs.palplayer.presentation.command_center

import androidx.lifecycle.SavedStateHandle
import com.hossainehs.palplayer.domain.model.MediaFile
import kotlinx.coroutines.flow.Flow

class CommandCenterState(
    private val savedStateHandle: SavedStateHandle
) {


    var currentlyPlayingName = savedStateHandle.getLiveData<String>("currentlyPlayingName")
        private set(value) {
            field = value
            savedStateHandle["currentlyPlayingName"] = value
        }

    fun updateCurrentlyPlayingName(name: String) {
        this.currentlyPlayingName.value = name
    }


}