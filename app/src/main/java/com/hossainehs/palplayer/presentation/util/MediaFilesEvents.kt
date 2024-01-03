package com.hossainehs.palplayer.presentation.util

import com.hossainehs.palplayer.domain.model.MediaFile

sealed class MediaFilesEvents{
    data class NavigateBack(
        val mainCategoryNumber: Int,
        ) : MediaFilesEvents()

    data class NavigateToSysTemMediaFiles(
        val subCategoryId: Int
        ) : MediaFilesEvents()


    data class PlayPauseMusic(
        val mediaFile: MediaFile
    ) : MediaFilesEvents()




}
