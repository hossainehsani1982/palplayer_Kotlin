package com.hossainehs.palplayer.domain.use_case

import com.hossainehs.palplayer.domain.model.Relation.SubCategoryWithMediaFile
import com.hossainehs.palplayer.domain.repository.Repository
import kotlinx.coroutines.flow.Flow

class GetSubCategoryWithMediaFilesUseCase(
    private val repository: Repository
) {
     operator fun invoke(
        mainCategoryId: Int
    ): Flow<SubCategoryWithMediaFile> {
         return  repository.getSubCategoryWithMediaFilesBySubCategoryId(mainCategoryId)
     }
}