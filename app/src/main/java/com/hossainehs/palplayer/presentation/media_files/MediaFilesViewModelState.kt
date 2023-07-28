package com.hossainehs.palplayer.presentation.media_files

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import com.hossainehs.mediaplayer.data.util.Resource
import com.hossainehs.palplayer.domain.model.MediaFile

class MediaFilesViewModelState(
    private val savedStateHandle: SavedStateHandle
) {

    var mediaItems: LiveData<Resource<List<MediaFile>>>? = null


    var mediaFilesList = savedStateHandle
        .getLiveData<List<MediaFile>>(
            "mediaFilesList"
        )
        private set(value) {
            field = value
            savedStateHandle["mediaFilesList"] = value
        }

    fun updateMediaFilesList(list: List<MediaFile>) {
        this.mediaFilesList.value = list
    }

    var subCategoryMainNumber = savedStateHandle
        .get<Int>(
            "subCategoryMainNumber"
        )
        private set(value) {
            field = value
            savedStateHandle["subCategoryMainNumber"] = value
        }

    fun updateSubCategoryMainNumber(id: Int) {
        this.subCategoryMainNumber = id
    }

}