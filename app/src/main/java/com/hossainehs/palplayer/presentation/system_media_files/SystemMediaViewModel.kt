package com.hossainehs.palplayer.presentation.system_media_files

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hossainehs.palplayer.domain.model.MediaFile
import com.hossainehs.palplayer.domain.model.SubCategory
import com.hossainehs.palplayer.domain.model.SystemMediaFile
import com.hossainehs.palplayer.domain.sharedPreferences.Preferences
import com.hossainehs.palplayer.domain.use_case.UseCases
import com.hossainehs.palplayer.presentation.util.SystemMediaEvents
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SystemMediaViewModel @Inject constructor(
    private val useCases: UseCases,
    savedStateHandle: SavedStateHandle,
    preferences: Preferences
) : ViewModel() {

    val state = SystemMediaViewModelState(savedStateHandle)
    val pref = preferences

    private val subCategoryId = savedStateHandle.get<Int>("subCategoryId") ?: 0

    private val _systemMediaEvents = Channel<SystemMediaEvents>()
    private lateinit var selectedMediaFile: MutableList<SystemMediaFile>
    val systemMediaEvents = _systemMediaEvents.receiveAsFlow()

    init {
        getSubCategoryWithMediaFiles(subCategoryId)

    }

    fun onEvents(events: SystemMediaViewModelEvents) {
        when (events) {
            is SystemMediaViewModelEvents.AddSelectedMediaFile -> {
                selectedMediaFile = state.selectedSystemMediaFileList
                selectedMediaFile.add(events.systemMediaFile)
                state.updateSelectedSystemFiles(selectedMediaFile)
            }

            is SystemMediaViewModelEvents.RemoveSelectedMediaFile -> {
                val selectedMediaFile = state.selectedSystemMediaFileList
                selectedMediaFile.remove(events.systemMediaFile)
                state.updateSelectedSystemFiles(selectedMediaFile)
            }

            SystemMediaViewModelEvents.OnBtnDoneClicked -> {
                if (state.selectedSystemMediaFileList.isNotEmpty()) {
                    viewModelScope.launch {
                        for (mediaFile in state.selectedSystemMediaFileList) {
                            useCases.insertMediaFileUseCase(
                                MediaFile(
                                    subCategoryId = mediaFile.subCategoryId,
                                    displayName = mediaFile.fileName,
                                    path = mediaFile.filePath,
                                    relativePath = mediaFile.fileRelativePath,
                                    duration = mediaFile.duration,
                                    artWorkUri = mediaFile.contentUri ?: "",
                                    isPlaying = false,
                                    album = mediaFile.album,
                                    artist = mediaFile.artist,
                                    track = mediaFile.track,
                                    isFavorite = false,
                                )
                            )
                        }

                    }
                }
                returnToMediaFilesFragment()

            }
        }
    }

    private fun getSubCategoryWithMediaFiles(subCategoryId: Int) {
        viewModelScope.launch {
            useCases.getSubCategoryWithMediaFilesUseCase(
                subCategoryId
            )?.let {
                state.updateCurrentSubCategory(
                    it.subCategory
                )
                getSystemMediaFiles()
            }

        }
    }

    private suspend fun getSystemMediaFiles() {
        useCases.getSystemMediaFilesUseCase(
            state.currentSubCategory!!
        ).collect {
            state.updateSubCatWithMediaFilesList(it)
        }
    }

    private fun returnToMediaFilesFragment() {
        viewModelScope.launch {
            _systemMediaEvents.send(SystemMediaEvents.OnBtnDoneClicked)
        }
    }


}