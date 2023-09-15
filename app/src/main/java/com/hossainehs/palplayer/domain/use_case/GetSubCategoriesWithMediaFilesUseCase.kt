package com.hossainehs.palplayer.domain.use_case

import com.hossainehs.palplayer.domain.repository.Repository

class GetSubCategoriesWithMediaFilesUseCase (
    private val repository: Repository
        ) {
    operator fun invoke(
        mainCategoryId: Int,
        mainCategoryName: String
    ) = repository.getSubCategoriesWithMediaFilesByMainCategoryName(
        mainCategoryId,
        mainCategoryName
    )
}