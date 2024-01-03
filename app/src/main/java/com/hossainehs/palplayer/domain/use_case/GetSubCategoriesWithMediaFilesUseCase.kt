package com.hossainehs.palplayer.domain.use_case

import com.hossainehs.palplayer.domain.model.relation.SubCategoryWithMediaFile
import com.hossainehs.palplayer.domain.repository.Repository
import kotlinx.coroutines.flow.Flow

class GetSubCategoriesWithMediaFilesUseCase(
    private val repository: Repository
) {
    operator fun invoke(
        mainCategoryId: Int,
    ): Flow<List<SubCategoryWithMediaFile>> {
       return repository.getSubCategoriesWithMediaFilesByMainCategoryName(
            mainCategoryId,
        )
    }
}