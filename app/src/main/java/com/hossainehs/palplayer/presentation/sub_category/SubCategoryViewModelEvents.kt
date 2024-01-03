package com.hossainehs.palplayer.presentation.sub_category

import com.hossainehs.palplayer.domain.model.relation.SubCategoryWithMediaFile

sealed class SubCategoryViewModelEvents {

    data class OnAddNewSubCategory(
        val subCategoryName: String
    ) : SubCategoryViewModelEvents()

    data class OnSubCategoryChanged(
        val mainCategoryNumber: Int
    ) : SubCategoryViewModelEvents()

    data class OnNavigateToMediaFiles(
        val subCategoryWithMediaFile: SubCategoryWithMediaFile
    ) : SubCategoryViewModelEvents()

    data class OnReturnToSubCategory(
        val mainCategoryNumber: Int
    ) : SubCategoryViewModelEvents()
}
