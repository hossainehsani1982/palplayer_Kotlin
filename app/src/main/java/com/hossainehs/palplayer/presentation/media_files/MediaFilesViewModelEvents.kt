package com.hossainehs.palplayer.presentation.media_files

import android.support.v4.media.MediaMetadataCompat
import com.hossainehs.palplayer.domain.model.MediaFile

sealed class MediaFilesViewModelEvents {
    data object NavigateBack : MediaFilesViewModelEvents()

    data object NavigateToSysTemMediaFiles : MediaFilesViewModelEvents()
    data object OnPlayPauseButtonClicked : MediaFilesViewModelEvents()

    data object OnPreviousButtonClicked: MediaFilesViewModelEvents()

    data object On30RewindButtonClicked: MediaFilesViewModelEvents()


    data object On10ForwardButtonClicked: MediaFilesViewModelEvents()

    data object OnNextButtonClicked : MediaFilesViewModelEvents()

    data class OnSelectedAudioChange(
        val mediaFile: MediaFile
    ): MediaFilesViewModelEvents()

    data class OnSeekTo(
        val position: Long
        ): MediaFilesViewModelEvents()

    data class UpdateProgress(
        val newProgress: Float
    ): MediaFilesViewModelEvents()




}