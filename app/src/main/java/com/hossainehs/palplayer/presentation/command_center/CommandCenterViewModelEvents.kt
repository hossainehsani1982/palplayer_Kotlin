package com.hossainehs.palplayer.presentation.command_center

import android.support.v4.media.MediaMetadataCompat
import com.hossainehs.palplayer.service.MusicService
import com.hossainehs.palplayer.domain.model.MediaFile

sealed class CommandCenterViewModelEvents {

    data class OnProgressbarChanged(
        val position: Long
        ): CommandCenterViewModelEvents()

    data class OnPreviousButtonClicked(
        val currentPosition : Long,
        val mediaFile: MediaFile
    ) : CommandCenterViewModelEvents()

    object On30RewindButtonClicked: CommandCenterViewModelEvents()

    data class OnPlayPauseButtonClicked(
        val currentPosition : Long?,
        val isPlaying: Boolean,
        val mediaFile: MediaFile
    ) : CommandCenterViewModelEvents()

    object On10ForwardButtonClicked: CommandCenterViewModelEvents()

    data class OnNextButtonClicked(
        val currentPosition : Long,
        val mediaFile: MediaFile
    ) : CommandCenterViewModelEvents()



    data class SetMusicService(
        val musicService: MusicService
    ) : CommandCenterViewModelEvents()

}