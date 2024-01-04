package com.hossainehs.palplayer.presentation.media_files

import androidx.lifecycle.SavedStateHandle
import com.hossainehs.palplayer.domain.model.MediaFile

class MediaFilesViewModelState(
    private val savedStateHandle: SavedStateHandle
) {



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


    var currentPosition = savedStateHandle.getLiveData<Long>("currentPosition")
        private set(value) {
            field = value
            savedStateHandle["currentPosition"] = value
        }
    fun updateCurrentPosition(currentPosition: Long) {
        this.currentPosition.value = currentPosition
    }


    var duration = savedStateHandle.get<Long>("duration") ?: 0L
        private set(value) {
            field = value
            savedStateHandle["duration"] = value
        }

    fun updateDuration(duration: Long) {
        this.duration = duration
    }

    var durationString = savedStateHandle.getLiveData<String>("durationString")
        private set(value) {
            field = value
            savedStateHandle["durationString"] = value
        }
    fun updateDurationString(progressString: String) {
        this.durationString.value = progressString
    }

    var progress = savedStateHandle.getLiveData<Float>("progress")
        private set(value) {
            field = value
            savedStateHandle["progress"] = value
        }
    fun updateProgress(progress: Float) {
        this.progress.value = progress
    }
    var progressString = savedStateHandle.getLiveData<String>("progressString")
        private set(value) {
            field = value
            savedStateHandle["progressString"] = value
        }
    fun updateProgressString(progressString: String) {
        this.progressString.value = progressString
    }
    var isPlaying = savedStateHandle.getLiveData<Boolean>("isPlaying")
        private set(value) {
            field = value
            savedStateHandle["isPlaying"] = value
        }
    fun updateIsPlaying(isPlaying: Boolean) {
        this.isPlaying.value = isPlaying
    }
//    private val dummyFile = MediaFile(
//        audioFileId = 0,
//        path = "",
//        displayName = "dummyFile",
//        artist = "",
//        duration = 0,
//    )

    var playingFileName = savedStateHandle.getLiveData<String>("playingFileName")
        private set(value) {
            field = value
            savedStateHandle["playingFileName"] = value
        }
    fun updatePlayingFileName(mediaFileName: String) {
        this.playingFileName.value = mediaFileName
    }


}