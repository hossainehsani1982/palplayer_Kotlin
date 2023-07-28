package com.hossainehs.palplayer.presentation.util

import com.hossainehs.palplayer.domain.model.MediaFile
import com.hossainehs.palplayer.domain.model.SubCategory

sealed class MediaFilesEvents{
    data class NavigateBack(
        val mainCategoryNumber: Int
        ) : MediaFilesEvents()

    data class NavigateToSysTemMediaFiles(
        val subCategoryId: Int
        ) : MediaFilesEvents()


    data class PlayMusic(
        val mediaFile: MediaFile
    ) : MediaFilesEvents()

    object PauseMusic : MediaFilesEvents()


}
