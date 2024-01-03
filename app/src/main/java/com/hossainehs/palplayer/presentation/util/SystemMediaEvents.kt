package com.hossainehs.palplayer.presentation.util

sealed class SystemMediaEvents {
    data class OnBtnDoneClicked(
        val isMediaAdded : Boolean
    ) : SystemMediaEvents()

}