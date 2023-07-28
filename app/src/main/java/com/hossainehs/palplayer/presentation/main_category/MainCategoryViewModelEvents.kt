package com.hossainehs.palplayer.presentation.main_category

import com.hossainehs.palplayer.domain.model.Relation.SubCategoryWithMediaFile
import com.hossainehs.palplayer.domain.model.SubCategory

sealed class MainCategoryViewModelEvents{

    data class OnAddNewSubCategory(
        val subCategoryName: String): MainCategoryViewModelEvents()

    data class OnMainCategoryChanged(
        val mainCategoryNumber: Int): MainCategoryViewModelEvents()

    data class OnNavigateToMediaFiles(
        val subCategoryWithMediaFile: SubCategoryWithMediaFile
        ) : MainCategoryViewModelEvents()
}
