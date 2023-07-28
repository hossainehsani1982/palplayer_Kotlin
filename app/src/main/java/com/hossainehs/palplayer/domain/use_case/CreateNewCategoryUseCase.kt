package com.hossainehs.palplayer.domain.use_case

import com.hossainehs.palplayer.domain.model.SubCategory
import com.hossainehs.palplayer.domain.repository.Repository

class CreateNewCategoryUseCase(
    private val repository: Repository
) {

    suspend operator fun invoke(
        subCategory: SubCategory
    ) {
        repository.insertNewSubCategory(
            subCategory
        )
    }
}