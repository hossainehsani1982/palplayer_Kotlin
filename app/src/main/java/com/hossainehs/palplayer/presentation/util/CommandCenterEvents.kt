package com.hossainehs.palplayer.presentation.util

import com.hossainehs.palplayer.domain.model.MediaFile

sealed class CommandCenterEvents {
    object OnPauseClick : CommandCenterEvents()
    object OnPlayClick : CommandCenterEvents()
    object OnNextClick : CommandCenterEvents()
    object OnPreviousClick : CommandCenterEvents()
    object On30SecRewindClick : CommandCenterEvents()
    object On10SecForwardClick : CommandCenterEvents()

    data class OnProgressbarChanged (
        val position: Long
        ): CommandCenterEvents()

    data class PlayPauseButtonAnimation(
        val isPlaying: Boolean
    ) : CommandCenterEvents()


}