package com.hossainehs.palplayer.domain.use_case

import com.hossainehs.palplayer.domain.model.MediaFile
import com.hossainehs.palplayer.domain.repository.Repository

class InsertMediaFileUseCase (
    private val repository: Repository
        ) {

    suspend operator fun invoke(mediaFile: MediaFile){
        repository.insertNewMediaFile(mediaFile)
    }
}