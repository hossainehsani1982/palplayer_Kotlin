package com.hossainehs.palplayer.domain.use_case

import com.hossainehs.palplayer.domain.repository.Repository

class GetSubCategoriesWithMediaFilesUseCase (
    private val repository: Repository
        ) {
    operator fun invoke(
        subCategoryId: Int,
        mainCategoryName: String
    ) = repository.getSubCategoriesWithMediaFilesByMainCategoryName(
        subCategoryId,
        mainCategoryName
    )
}