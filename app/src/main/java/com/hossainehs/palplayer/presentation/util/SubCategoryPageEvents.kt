package com.hossainehs.palplayer.presentation.util

import com.hossainehs.palplayer.domain.model.SubCategory

sealed class SubCategoryPageEvents{

    data class NavigateToMediaFiles(
        val subCategoryId : Int
        ) : SubCategoryPageEvents()
}
