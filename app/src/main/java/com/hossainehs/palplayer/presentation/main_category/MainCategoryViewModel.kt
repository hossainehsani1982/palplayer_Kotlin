package com.hossainehs.palplayer.presentation.main_category

import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hossainehs.mediaplayer.data.util.Resource
import com.hossainehs.palplayer.data.util.ConstValues
import com.hossainehs.palplayer.data.util.ConstValues.GET_SUB_CATEGORIES
import com.hossainehs.palplayer.data.util.ConstValues.MAIN_CATEGORY_NAME
import com.hossainehs.palplayer.data.util.ConstValues.MAIN_CATEGORY_NUMBER
import com.hossainehs.palplayer.data.util.ConstValues.PLAYLIST_ROOT_ID
import com.hossainehs.palplayer.domain.model.MediaFile
import com.hossainehs.palplayer.domain.model.Relation.SubCategoryWithMediaFile
import com.hossainehs.palplayer.domain.model.SubCategory
import com.hossainehs.palplayer.domain.sharedPreferences.Preferences
import com.hossainehs.palplayer.domain.use_case.UseCases
import com.hossainehs.palplayer.presentation.util.SubCategoryPageEvents
import com.hossainehs.palplayer.service.MusicServiceConnection
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class MainCategoryViewModel @Inject constructor(
    private val useCases: UseCases,
    savedStateHandle: SavedStateHandle,
    preferences: Preferences,
    private val musicServiceConnection: MusicServiceConnection
) : ViewModel() {

    private val _mainCategoryEvents = Channel<SubCategoryPageEvents>()
    val mainCategoryEvents = _mainCategoryEvents.receiveAsFlow()
    val state = MainCategoryVieModelState(savedStateHandle)
    val pref = preferences
    private val _subCategoryItems = MutableLiveData<Resource<List<SubCategoryWithMediaFile>>>()
    val subCategoryItems: LiveData<Resource<List<SubCategoryWithMediaFile>>> = _subCategoryItems

    init {
        loadData()
    }

    fun onEvents(events: MainCategoryViewModelEvents) {
        when (events) {
            is MainCategoryViewModelEvents.OnMainCategoryChanged -> {
                when (events.mainCategoryNumber) {
                    1 -> {
                        println("mCatViewModel, music")
                        state.updateMainCategoryName("Music")
                        state.updateMainCategoryNumber(1)
                        musicServiceConnection.unsubscribe(
                            PLAYLIST_ROOT_ID,
                            object : MediaBrowserCompat.SubscriptionCallback() {}
                        )
                        val args = Bundle()
                        args.putInt(MAIN_CATEGORY_NUMBER, 1)
                        args.putString(MAIN_CATEGORY_NAME, "Music")
                        musicServiceConnection.sendCommand(GET_SUB_CATEGORIES, args)

                    }

                    2 -> {
                        println("mCatViewModel, audio books")
                        state.updateMainCategoryName("AudioBooks")
                        state.updateMainCategoryNumber(2)
                        musicServiceConnection.unsubscribe(
                            PLAYLIST_ROOT_ID,
                            object : MediaBrowserCompat.SubscriptionCallback() {}
                        )
                        val args = Bundle()
                        args.putInt(MAIN_CATEGORY_NUMBER, 2)
                        args.putString(MAIN_CATEGORY_NAME, "AudioBooks")
                        musicServiceConnection.sendCommand(GET_SUB_CATEGORIES, args)


                    }

                    3 -> {
                        musicServiceConnection.unsubscribe(
                            PLAYLIST_ROOT_ID,
                            object : MediaBrowserCompat.SubscriptionCallback() {}
                        )
                        println("mCatViewModel, recordings")
                        state.updateMainCategoryName("Recordings")
                        state.updateMainCategoryNumber(3)
                        val args = Bundle()
                        args.putInt(MAIN_CATEGORY_NUMBER, 3)
                        args.putString(MAIN_CATEGORY_NAME, "Recordings")
                        musicServiceConnection.sendCommand(GET_SUB_CATEGORIES, args)
                    }

                    4 -> {
                        musicServiceConnection.unsubscribe(
                            PLAYLIST_ROOT_ID,
                            object : MediaBrowserCompat.SubscriptionCallback() {}
                        )
                        println("mCatViewModel, podcast")
                        state.updateMainCategoryName("Podcast")
                        state.updateMainCategoryNumber(4)
                        val args = Bundle()
                        args.putInt(MAIN_CATEGORY_NUMBER, 4)
                        args.putString(MAIN_CATEGORY_NAME, "Podcast")
                        musicServiceConnection.sendCommand(GET_SUB_CATEGORIES, args)
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


    private fun loadData() {
        _subCategoryItems.postValue(Resource.loading(null))
        musicServiceConnection.subscribe(
            PLAYLIST_ROOT_ID,
            object : MediaBrowserCompat.SubscriptionCallback() {
                override fun onChildrenLoaded(
                    parentId: String,
                    children: MutableList<MediaBrowserCompat.MediaItem>
                ) {
                    viewModelScope.launch {
                        var items = children.map {
                            getSubCategoryWithMediaId(
                                it.mediaId!!.toInt()
                            )
                        }
                        _subCategoryItems.postValue(
                            Resource.success(
                                items
                            )
                        )
                    }
                }
            }
        )
    }

    private suspend fun getSubCategoryWithMediaId(
        subCategoryId: Int
    ): SubCategoryWithMediaFile {
        return useCases.getSubCategoryWithMediaFilesUseCase(
            subCategoryId
        )
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

