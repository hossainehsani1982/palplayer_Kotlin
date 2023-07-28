package com.hossainehs.palplayer.presentation.media_files

import android.support.v4.media.MediaMetadataCompat
import com.hossainehs.palplayer.domain.model.MediaFile
import com.hossainehs.palplayer.presentation.command_center.CommandCenterViewModelEvents

sealed class MediaFilesViewModelEvents {
    object NavigateBack : MediaFilesViewModelEvents()

    object NavigateToSysTemMediaFiles : MediaFilesViewModelEvents()
    data class OnPlayPauseButtonClicked(
        val mediaItem: MediaFile,
        var toggle: Boolean = false
    ) : MediaFilesViewModelEvents()

    object OnPreviousButtonClicked: MediaFilesViewModelEvents()

    object On30RewindButtonClicked: MediaFilesViewModelEvents()


    object On10ForwardButtonClicked: MediaFilesViewModelEvents()

    object OnNextButtonClicked : MediaFilesViewModelEvents()

    data class OnSeekTo(
        val position: Long
        ): MediaFilesViewModelEvents()

    data class OnMediaItemCompatChanged(
        val mediaMetadataCompat: MediaMetadataCompat
    ): MediaFilesViewModelEvents()




}