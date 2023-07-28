package com.hossainehs.palplayer.presentation.system_media_files

import com.hossainehs.palplayer.domain.model.SystemMediaFile

sealed class SystemMediaViewModelEvents{
    data class AddSelectedMediaFile(
        val systemMediaFile: SystemMediaFile
    ) : SystemMediaViewModelEvents()

    data class RemoveSelectedMediaFile(
        val systemMediaFile: SystemMediaFile
    ) : SystemMediaViewModelEvents()

    object OnBtnDoneClicked : SystemMediaViewModelEvents()
}
