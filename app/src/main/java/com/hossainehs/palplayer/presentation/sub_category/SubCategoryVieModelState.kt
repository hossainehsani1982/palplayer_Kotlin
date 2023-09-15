package com.hossainehs.palplayer.presentation.sub_category

import androidx.lifecycle.SavedStateHandle
import com.hossainehs.palplayer.domain.model.Relation.SubCategoryWithMediaFile

class SubCategoryVieModelState(
    private val savedStateHandle: SavedStateHandle
) {


    var subCategoryWithMediaFilesList = savedStateHandle
        .getLiveData<List<SubCategoryWithMediaFile>>(
            "mainCategoryWithMediaFilesList"
        )
        private set(value) {
            field = value
            savedStateHandle["mainCategoryWithMediaFilesList"] = value
        }

    fun updateSubCategoryWithMediaFilesList(list: List<SubCategoryWithMediaFile>) {
        this.subCategoryWithMediaFilesList.value = list
    }

     //   var subCategoriesWithMediaFilesList : List<SubCategoryWithMediaFile> = listOf()






    var mainCategoryName = savedStateHandle
        .get<String>("mainCategoryName") ?: "home"
        private set(value) {
            field = value
            savedStateHandle["mainCategoryName"] = value
        }

    fun updateMainCategoryName(name: String) {
        this.mainCategoryName = name
    }

    var mainCategoryNumber = savedStateHandle
        .get<Int>("mainCategoryNumber") ?: 0
        private set(value) {
            field = value
            savedStateHandle["mainCategoryNumber"] = value
        }

    fun updateMainCategoryNumber(number: Int) {
        this.mainCategoryNumber = number
    }


}