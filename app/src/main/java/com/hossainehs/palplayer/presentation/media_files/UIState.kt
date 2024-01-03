package com.hossainehs.palplayer.presentation.media_files

sealed class UIState {
    object Initial : UIState()
    object Ready : UIState()
}