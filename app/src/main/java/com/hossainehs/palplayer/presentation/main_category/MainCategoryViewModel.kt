package com.hossainehs.palplayer.presentation.main_category

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hossainehs.palplayer.domain.model.SubCategory
import com.hossainehs.palplayer.domain.sharedPreferences.Preferences
import com.hossainehs.palplayer.domain.use_case.UseCases
import com.hossainehs.palplayer.presentation.util.SubCategoryPageEvents
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainCategoryViewModel @Inject constructor(
    private val useCases: UseCases,
    savedStateHandle: SavedStateHandle,
    preferences: Preferences
) : ViewModel() {

    private val _mainCategoryEvents = Channel<SubCategoryPageEvents>()
    val mainCategoryEvents = _mainCategoryEvents.receiveAsFlow()
    val state = MainCategoryVieModelState(savedStateHandle)
    val pref = preferences




    fun onEvents(events: MainCategoryViewModelEvents) {
        when (events) {
            is MainCategoryViewModelEvents.OnMainCategoryChanged -> {
                when (events.mainCategoryNumber) {
                    1 -> {
                        println("mCatViewModel, music")
                        state.updateMainCategoryName("Music")
                        state.updateMainCategoryNumber(1)
                        getSubCategoriesWithMediaFiles(
                            1,
                            "Music")
                    }

                    2 -> {
                        println("mCatViewModel, audio books")
                        state.updateMainCategoryName("AudioBooks")
                        state.updateMainCategoryNumber(2)
                        getSubCategoriesWithMediaFiles(
                            2,
                            "AudioBooks")
                    }

                    3 -> {
                        println("mCatViewModel, recordings")
                        state.updateMainCategoryName("Recordings")
                        state.updateMainCategoryNumber(3)
                        getSubCategoriesWithMediaFiles(
                            3,
                            "Recordings")
                    }

                    4 -> {
                        println("mCatViewModel, podcast")
                        state.updateMainCategoryName("Podcast")
                        state.updateMainCategoryNumber(4)
                        getSubCategoriesWithMediaFiles(
                            4,
                            "Podcast")
                    }
                }
            }

            is MainCategoryViewModelEvents.OnAddNewSubCategory -> {
                createNewCategory(
                    events.subCategoryName
                )
            }

            is MainCategoryViewModelEvents.OnNavigateToMediaFiles -> {
                val m = events.subCategoryWithMediaFile.subCategory.subCategoryId
                viewModelScope.launch {
                    _mainCategoryEvents.send(
                        SubCategoryPageEvents.NavigateToMediaFiles(
                            events.subCategoryWithMediaFile.subCategory.subCategoryId ?: 0
                        )
                    )
                }
            }
        }
    }


    private fun getSubCategoriesWithMediaFiles(
        subCategoryId: Int,
        mainCategoryName : String) {
        println("mCatViewModel, state: $mainCategoryName")
        viewModelScope.launch {
            useCases.getSubCategoriesWithMediaFilesUseCase(
                subCategoryId,
                mainCategoryName
            ).collectLatest{
                state.updateSubCategoryWithMediaFilesList(it)
                println("mCatViewModel, list: ${it.size}")
            }

        }
    }

    private fun createNewCategory(subCategoryName: String) {
        viewModelScope.launch {
            val newSubCategory = SubCategory(
                mainCategoryName = state.mainCategoryName,
                mainCategoryNumber = state.mainCategoryNumber,
                name = subCategoryName,
                filesCount = 0
            )
            useCases.createNewCategoryUseCase(newSubCategory)
        }
    }
}

