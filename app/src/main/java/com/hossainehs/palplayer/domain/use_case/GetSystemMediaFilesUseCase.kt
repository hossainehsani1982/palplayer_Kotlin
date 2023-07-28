package com.hossainehs.palplayer.domain.use_case

import com.hossainehs.palplayer.domain.model.SubCategory
import com.hossainehs.palplayer.domain.model.SystemMediaFile
import com.hossainehs.palplayer.domain.repository.Repository
import kotlinx.coroutines.flow.Flow

class GetSystemMediaFilesUseCase (
    private val repository: Repository
        ) {

    suspend operator fun invoke(
        subCategory: SubCategory
    ) : Flow<List<SystemMediaFile>> {
       return repository.getMediaFilesFromDevice(subCategory)
    }
}