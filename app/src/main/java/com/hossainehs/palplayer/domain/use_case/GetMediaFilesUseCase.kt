package com.hossainehs.palplayer.domain.use_case

import com.hossainehs.palplayer.domain.model.MediaFile
import com.hossainehs.palplayer.domain.repository.Repository
import kotlinx.coroutines.flow.Flow

class GetMediaFilesUseCase(
    private val repository: Repository
) {
    operator fun invoke(
        subCategoryId: Int
    ): Flow<List<MediaFile>> {
        return repository.getMediaFilesBySubCategoryId(subCategoryId)
    }
}