package com.hossainehs.palplayer.presentation.system_media_files

import androidx.lifecycle.SavedStateHandle
import com.hossainehs.palplayer.domain.model.SubCategory
import com.hossainehs.palplayer.domain.model.SystemMediaFile

class SystemMediaViewModelState (
    private val savedStateHandle: SavedStateHandle
        ) {

    var systemMediaFileList = savedStateHandle
        .getLiveData<List<SystemMediaFile>>(
            "systemMediaFiles"
        )
        private set(value) {
            field = value
            savedStateHandle["systemMediaFiles"] = value
        }

    fun updateSubCatWithMediaFilesList(list: List<SystemMediaFile>) {
        this.systemMediaFileList.value = list
    }

    var currentSubCategory = savedStateHandle
        .get<SubCategory>(
            "currentSubCategory"
        )
        private set(value) {
            field = value
            savedStateHandle["currentSubCategory"] = value
        }

    fun updateCurrentSubCategory(list: SubCategory) {
        this.currentSubCategory = list
    }

    var selectedSystemMediaFileList = savedStateHandle
        .get<MutableList<SystemMediaFile>>(
            "selectedSystemMediaFiles"
        ) ?: mutableListOf()
        private set(value) {
            field = value
            savedStateHandle["selectedSystemMediaFiles"] = value
        }

    fun updateSelectedSystemFiles(list: MutableList<SystemMediaFile>) {
        this.selectedSystemMediaFileList = list
    }





}