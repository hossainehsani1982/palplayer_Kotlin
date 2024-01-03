package com.hossainehs.palplayer.domain.use_case

import com.hossainehs.palplayer.domain.repository.Repository

class GetMediaFileByUri(
    private val repository: Repository
) {
    suspend operator fun invoke(
        uri: String
    ) =
         repository.getMediaFileByUri(uri)

}