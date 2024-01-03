package com.hossainehs.palplayer.presentation.sub_category

import android.support.v4.media.MediaBrowserCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.hossainehs.palplayer.domain.model.relation.SubCategoryWithMediaFile
import com.hossainehs.palplayer.domain.model.SubCategory
import com.hossainehs.palplayer.domain.sharedPreferences.Preferences
import com.hossainehs.palplayer.domain.use_case.UseCases
import com.hossainehs.palplayer.media_item_service.AppMediaSource
import com.hossainehs.palplayer.media_item_service.MediaBrowserConnection
import com.hossainehs.palplayer.presentation.util.SubCategoryPageEvents
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubCategoryViewModel @Inject constructor(
    private val useCases: UseCases,
    savedStateHandle: SavedStateHandle,
    preferences: Preferences,
    private val mediaBrowserConnection: MediaBrowserConnection,
    private val appMediaSource: AppMediaSource
) : ViewModel() {

    private val _mainCategoryEvents = Channel<SubCategoryPageEvents>()
    val mainCategoryEvents = _mainCategoryEvents.receiveAsFlow()
    private val state = SubCategoryVieModelState(savedStateHandle)
    val pref = preferences


    var subCategoryItems: Flow<List<SubCategoryWithMediaFile>>? = null


    fun onEvents(events: SubCategoryViewModelEvents) {
        when (events) {
            is SubCategoryViewModelEvents.OnSubCategoryChanged -> {
                when (events.mainCategoryNumber) {
                    1 -> {
                        println("mCatViewModel, music")
                        state.updateMainCategoryName("Music")
                        state.updateMainCategoryNumber(1)
                        viewModelScope.launch {
                            getSubCategoriesWithMediaId(
                                subCatId = 1,
                            )
                        }


                    }

                    2 -> {
                        println("mCatViewModel, audio books")
                        state.updateMainCategoryName("AudioBooks")
                        state.updateMainCategoryNumber(2)
                        viewModelScope.launch {
                            getSubCategoriesWithMediaId(
                                subCatId = 2,
                            )
                        }


                    }

                    3 -> {
                        println("mCatViewModel, recordings")
                        state.updateMainCategoryName("Recordings")
                        state.updateMainCategoryNumber(3)
                        viewModelScope.launch {
                            getSubCategoriesWithMediaId(
                                subCatId = 3,
                            )
                        }


                    }

                    4 -> {
                        println("mCatViewModel, podcast")
                        state.updateMainCategoryName("Podcast")
                        state.updateMainCategoryNumber(4)
                        viewModelScope.launch {
                            getSubCategoriesWithMediaId(
                                subCatId = 4,
                            )
                        }


                    }
                }
            }

            is SubCategoryViewModelEvents.OnAddNewSubCategory -> {
                createNewCategory(
                    events.subCategoryName
                )


            }

            is SubCategoryViewModelEvents.OnNavigateToMediaFiles -> {
                viewModelScope.launch {
                    _mainCategoryEvents.send(
                        SubCategoryPageEvents.NavigateToMediaFiles(
                            events.subCategoryWithMediaFile.subCategory.subCategoryId ?: 0
                        )
                    )
                }
            }

            is SubCategoryViewModelEvents.OnReturnToSubCategory -> {
                viewModelScope.launch {
                    subCategoryItems?.collect {
                        _mainCategoryEvents.send(
                            SubCategoryPageEvents.LoadSubCategories(
                                it
                            )
                        )
                    }
                }
            }
        }
    }


    private suspend fun getSubCategoriesWithMediaId(
        subCatId: Int,
    ) {
        subCategoryItems = useCases.getSubCategoriesWithMediaFilesUseCase(
            mainCategoryId = subCatId,
        )

        viewModelScope.launch {
            subCategoryItems?.let { flowList ->
                flowList.collect { subCatList ->
                    _mainCategoryEvents.send(
                        SubCategoryPageEvents.LoadSubCategories(
                            subCatList
                        )
                    )
                }
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


//    private fun loadData(parentId: String) {
//        mediaBrowserConnection.subscribe(
//            parentId = parentId,
//            callback = object : MediaBrowserCompat.SubscriptionCallback() {
//                override fun onChildrenLoaded(
//                    parentId: String,
//                    children: MutableList<MediaBrowserCompat.MediaItem>
//                ) {
//                    viewModelScope.launch {
//                        println("mCatViewModel, onChildrenLoaded")
//                        println("children :  $children")
//                        val items: List<SubCategoryWithMediaFile> = children.map { mediaItem ->
//                            useCases.getSubCategoryWithMediaFilesUseCase(
//                                mainCategoryId = mediaItem.mediaId!!.toInt()
//                            )
//                        }
//                        println("items :  $items")
//
//                        // Update LiveData with the results
//                        //_subCategoryItems.postValue(items)
//                    }
//                }
//            }
//        )
//    }
}

