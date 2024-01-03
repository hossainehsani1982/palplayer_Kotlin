package com.hossainehs.palplayer.presentation.media_files


import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.hossainehs.palplayer.domain.model.MediaFile
import com.hossainehs.palplayer.domain.use_case.UseCases
import com.hossainehs.palplayer.player_service.AppAudioState
import com.hossainehs.palplayer.player_service.AppPlayerEvents
import com.hossainehs.palplayer.player_service.AudioServicePlaybackHandler
import com.hossainehs.palplayer.presentation.util.MediaFilesEvents
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject


@HiltViewModel
class MediaFilesViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val useCases: UseCases,
    private val audioServicePlaybackHandler: AudioServicePlaybackHandler
) : ViewModel() {

    private val subCategoryId = savedStateHandle.get<Int>("subCategoryId") ?: 1
    val state = MediaFilesViewModelState(savedStateHandle)

    private val _mediaFilesEvents = Channel<MediaFilesEvents>()
    val mediaFilesEvents = _mediaFilesEvents.receiveAsFlow()


    private val _uiState: MutableStateFlow<UIState> = MutableStateFlow(UIState.Initial)
    val uiState: StateFlow<UIState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            loadMediaFiles()
            /**
            collecting audio state from audio service handler
            -> audioState: StateFlow<AppAudioState>

             **/
            audioServicePlaybackHandler.audioState.collectLatest { appAudioState ->

                when (appAudioState) {
                    AppAudioState.Initializing -> {
                        _uiState.value = UIState.Initial
                    }

                    is AppAudioState.Buffering -> {
                        calculateProgress(appAudioState.progress)
                    }

                    is AppAudioState.Playing -> {
                        state.updateIsPlaying(appAudioState.isPlaying)
                    }

                    is AppAudioState.Progress -> {
                        calculateProgress(appAudioState.progress)
                    }

                    is AppAudioState.CurrentlyPlaying -> {
                        state.mediaFilesList.value?.let {
                            state.updateCurrentPlayingFile(it[appAudioState.mediaItemIndex])
                        }

                    }

                    is AppAudioState.Ready -> {
                        state.updateDuration(
                            appAudioState.duration
                        )

                        state.updateDurationString(
                            formatDuration(
                                appAudioState.duration
                            )
                        )

                        _uiState.value = UIState.Ready
                    }
                }
            }

        }
    }


    fun onEvent(events: MediaFilesViewModelEvents) {
        when (events) {
            MediaFilesViewModelEvents.NavigateBack -> {
                viewModelScope.launch {
                    _mediaFilesEvents.send(
                        MediaFilesEvents.NavigateBack(
                            mainCategoryNumber = state.subCategoryMainNumber ?: 1,
                        )
                    )
                }
            }

            MediaFilesViewModelEvents.NavigateToSysTemMediaFiles -> {
                viewModelScope.launch {
                    _mediaFilesEvents.send(
                        MediaFilesEvents.NavigateToSysTemMediaFiles(
                            subCategoryId
                        )
                    )
                }
            }

            is MediaFilesViewModelEvents.OnSelectedAudioChange -> {
                viewModelScope.launch {
                    audioServicePlaybackHandler.onPlayerEvents(
                        AppPlayerEvents.SelectedAudioChanged,
                        selectedAudioIndex = state.mediaFilesList.value!!.indexOf(events.mediaFile)
                    )
                }
            }

            is MediaFilesViewModelEvents.OnPreviousButtonClicked -> {
                viewModelScope.launch {
                    audioServicePlaybackHandler.onPlayerEvents(
                        AppPlayerEvents.SeekToPrevious
                    )
                }

            }

            MediaFilesViewModelEvents.On30RewindButtonClicked -> {

            }

            is MediaFilesViewModelEvents.OnPlayPauseButtonClicked -> {
                viewModelScope.launch {
                    audioServicePlaybackHandler.onPlayerEvents(
                        AppPlayerEvents.PlayPause
                    )
                }
            }

            MediaFilesViewModelEvents.On10ForwardButtonClicked -> {


            }

            is MediaFilesViewModelEvents.OnNextButtonClicked -> {
                viewModelScope.launch {
                    audioServicePlaybackHandler.onPlayerEvents(
                        AppPlayerEvents.SeekToNext
                    )
                }
            }

            is MediaFilesViewModelEvents.OnSeekTo -> {
                println("seekTo: ${events.position}")
                viewModelScope.launch {
                    audioServicePlaybackHandler.onPlayerEvents(
                        AppPlayerEvents.SeekTo,
                        seekPosition = events.position
                    )
                }
            }

            is MediaFilesViewModelEvents.UpdateProgress -> {
                viewModelScope.launch {
                    audioServicePlaybackHandler.onPlayerEvents(
                        AppPlayerEvents.UpdateProgress(
                            events.newProgress
                        )
                    )
                    state.updateProgress(events.newProgress)
                }
            }

            MediaFilesViewModelEvents.OnMediaAdded -> {
                viewModelScope.launch {
                    loadMediaFiles()
                }
            }
        }
    }

    private suspend fun loadMediaFiles() {
        useCases.getSubCategoryWithMediaFilesUseCase(subCategoryId)?.let { subCatWithMediaFiles ->
            state.updateMediaFilesList(
                subCatWithMediaFiles.mediaFiles
            )
            state.mediaFilesList.value?.let { listMediaFiles ->
                setMediaItems(listMediaFiles)
            }
        }


    }

    private fun setMediaItems(playList: List<MediaFile>) {
        playList.map {
            playList.map { audio ->
                MediaItem.Builder()
                    .setUri(audio.path)
                    .setMediaId(audio.audioFileId.toString())
                    .setMediaMetadata(
                        MediaMetadata.Builder()
                            .setAlbumArtist(audio.artist)
                            .setDisplayTitle(audio.displayName)
                            .setSubtitle(audio.displayName)
                            .build()
                    )
                    .build()
            }.also {
                audioServicePlaybackHandler.setMediaItemList(it)
            }
        }
    }

    private fun calculateProgress(currentPosition: Long) {
        if (state.duration > 0) {
            state.updateProgress((currentPosition.toFloat() / state.duration.toFloat()) * 100f)
            println(state.progress)
        } else {
            // Handle the case where the duration is not available yet
            state.updateProgress(0f)
        }
        state.updateProgressString(formatDuration(currentPosition))
    }

    private fun formatDuration(duration: Long): String {
        println("durationFormatDuration: $duration")

        val hours = TimeUnit.MILLISECONDS.toHours(duration)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(duration) -
                TimeUnit.HOURS.toMinutes(hours)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(duration) -
                TimeUnit.MINUTES.toSeconds(minutes) -
                TimeUnit.HOURS.toSeconds(hours)

        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

    override fun onCleared() {
        viewModelScope.launch {
            audioServicePlaybackHandler.onPlayerEvents(AppPlayerEvents.Stop)
        }
        super.onCleared()
    }


}



