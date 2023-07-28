package com.hossainehs.palplayer.domain.use_case

import androidx.lifecycle.LiveData
import com.hossainehs.palplayer.domain.model.MediaFile
import com.hossainehs.palplayer.domain.repository.Repository
import java.util.concurrent.Flow

class GetMediaFileByUri(
    private val repository: Repository
) {
    suspend operator fun invoke(
        uri: String
    ) =
         repository.getMediaFileByUri(uri)

}