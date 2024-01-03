package com.hossainehs.palplayer.presentation.util

import com.hossainehs.palplayer.domain.model.relation.SubCategoryWithMediaFile
import kotlinx.coroutines.flow.Flow

sealed class SubCategoryPageEvents{
    data class LoadSubCategories(
        val subCategories : List<SubCategoryWithMediaFile>
    ) : SubCategoryPageEvents()

    data class NavigateToMediaFiles(
        val subCategoryId : Int
        ) : SubCategoryPageEvents()
}
