package com.hossainehs.palplayer.presentation.util

sealed class CommandCenterEvents {
    object OnPauseClick : CommandCenterEvents()
    object OnPlayPauseClick : CommandCenterEvents()
    object OnNextClick : CommandCenterEvents()
    object OnPreviousClick : CommandCenterEvents()
    object On30SecRewindClick : CommandCenterEvents()
    object On10SecForwardClick : CommandCenterEvents()

    data class OnProgressbarChanged (
        val position: Long
        ): CommandCenterEvents()




}