package com.hossainehs.palplayer.presentation.media_files

import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.exoplayer2.ExoPlayer
import com.hossainehs.mediaplayer.data.util.Resource
import com.hossainehs.palplayer.data.util.ConstValues.MEDIA_ROOT_ID
import com.hossainehs.palplayer.domain.model.MediaFile
import com.hossainehs.palplayer.domain.use_case.UseCases
import com.hossainehs.palplayer.presentation.util.MediaFilesEvents
import com.hossainehs.palplayer.service.MusicServiceConnection
import com.hossainehs.palplayer.service.isPlayEnabled
import com.hossainehs.palplayer.service.isPlaying
import com.hossainehs.palplayer.service.isPrepared
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MediaFilesViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val musicServiceConnection: MusicServiceConnection,
    private val useCases: UseCases,
    private val exoPlayer: ExoPlayer
) : ViewModel() {
    private val subCategoryId = savedStateHandle.get<Int>("subCategoryId") ?: 1
    val state = MediaFilesViewModelState(savedStateHandle)

    private val _mediaFilesEvents = Channel<MediaFilesEvents>()
    val mediaFilesEvents = _mediaFilesEvents.receiveAsFlow()


    private val _mediaItems = MutableLiveData<Resource<List<MediaFile>>>()
    val mediaItems: LiveData<Resource<List<MediaFile>>> = _mediaItems
    private val _isPlaying = MutableLiveData<Boolean>()
    val isMediaFilePlaying: LiveData<Boolean> = _isPlaying
    val isConnected = musicServiceConnection.isConnected
    val databaseError = musicServiceConnection.databaseError
    val playbackState = musicServiceConnection.playbackState
    val currentPlayingSong = musicServiceConnection.currentPlayingSong
    private val _currentlyPlayingMediaFile = MutableLiveData<MediaFile>()
    var currentlyPlayingMediaFile: LiveData<MediaFile>? = _currentlyPlayingMediaFile
    var currentPosition = MutableLiveData<Long>()

    init {
        _mediaItems.postValue(Resource.loading(null))
        musicServiceConnection.subscribe(
            MEDIA_ROOT_ID,
            object : MediaBrowserCompat.SubscriptionCallback() {
                override fun onChildrenLoaded(
                    parentId: String,
                    children: MutableList<MediaBrowserCompat.MediaItem>
                ) {
                    super.onChildrenLoaded(parentId, children)
                    println(children)
                    val items = children.map {
                        MediaFile(
                            audioFileId = it.mediaId?.toInt(),
                            subCategoryId = subCategoryId,
                            path = it.description.mediaUri.toString(),
                            artWorkUri = it.description.iconUri.toString(),
                            duration = it.description.extras?.getLong(
                                MediaMetadataCompat.METADATA_KEY_DURATION
                            ) ?: 0L,
                            isPlaying = false,
                            album = it.description.extras?.getString(
                                MediaMetadataCompat.METADATA_KEY_ALBUM
                            ) ?: "",
                            artist = it.description.extras?.getString(
                                MediaMetadataCompat.METADATA_KEY_ARTIST
                            ) ?: "",
                            displayName = it.description.extras?.getString(
                                MediaMetadataCompat.METADATA_KEY_DISPLAY_TITLE
                            ) ?: "",
                            track = it.description.extras?.getInt(
                                MediaMetadataCompat.METADATA_KEY_TRACK_NUMBER
                            ) ?: 1,
                            isFavorite = false,
                        )
                    }
                    _mediaItems.postValue(Resource.success(items))

                }
            }
        )
    }

    fun onEvent(events: MediaFilesViewModelEvents) {
        when (events) {
            MediaFilesViewModelEvents.NavigateBack -> {
                viewModelScope.launch {
                    _mediaFilesEvents.send(
                        MediaFilesEvents.NavigateBack(
                            state.subCategoryMainNumber ?: 1

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

            is MediaFilesViewModelEvents.OnPreviousButtonClicked -> {
                skipToPreviousSong()
            }

            MediaFilesViewModelEvents.On30RewindButtonClicked -> {
                seekTo(exoPlayer.currentPosition - 30000)
            }

            is MediaFilesViewModelEvents.OnPlayPauseButtonClicked -> {

                println("toggle ${events.toggle}")
                //check if player is prepared
                val isPrepared = playbackState.value?.isPrepared ?: false
                // for same song
                if (isPrepared && events.mediaItem.audioFileId.toString() ==
                    currentPlayingSong.value?.getString(
                        MediaMetadataCompat.METADATA_KEY_MEDIA_ID
                    )
                ) {
                    println("same song")
                    playbackState.value?.let { playbackState ->
                        when {
                            playbackState.isPlaying -> {
                                if (events.toggle) {
                                    // pause the song
                                    println("pause the song ${events.toggle}")
                                    musicServiceConnection.transportControls.pause()
                                    isMusicPlaying(
                                        events.mediaItem,
                                        false
                                    )

                                }
                            }

                            playbackState.isPlayEnabled -> {
                                //resume same song
                                println("resume same song ${events.toggle}")
                                musicServiceConnection.transportControls.play()
                                isMusicPlaying(
                                    events.mediaItem,
                                    true
                                )
                            }

                            else -> Unit
                        }
                    }
                    //for new song
                } else {
                    println("new song")
                    musicServiceConnection.transportControls.prepareFromMediaId(
                        events.mediaItem.audioFileId.toString(),
                        null
                    )
                    isMusicPlaying(
                        events.mediaItem,
                        true
                    )
                }
                _currentlyPlayingMediaFile?.postValue(events.mediaItem)

            }

            MediaFilesViewModelEvents.On10ForwardButtonClicked -> {
                seekTo(exoPlayer.currentPosition + 10000)
            }

            is MediaFilesViewModelEvents.OnNextButtonClicked -> {
                skipToNextSong()
            }

            is MediaFilesViewModelEvents.OnSeekTo -> {
                seekTo(events.position)
            }

            is MediaFilesViewModelEvents.OnMediaItemCompatChanged -> {
                _currentlyPlayingMediaFile?.postValue(
                    MediaFile(
                        audioFileId = events.mediaMetadataCompat.description.mediaId?.toInt(),
                        subCategoryId = subCategoryId,
                        path = events.mediaMetadataCompat.description.mediaUri.toString(),
                        artWorkUri = events.mediaMetadataCompat.description.iconUri.toString(),
                        duration = events.mediaMetadataCompat.description.extras?.getLong(
                            MediaMetadataCompat.METADATA_KEY_DURATION
                        ) ?: 0L,
                        isPlaying = false,
                        album = events.mediaMetadataCompat.description.extras?.getString(
                            MediaMetadataCompat.METADATA_KEY_ALBUM
                        ) ?: "",
                        artist = events.mediaMetadataCompat.description.extras?.getString(
                            MediaMetadataCompat.METADATA_KEY_ARTIST
                        ) ?: "",
                        displayName = events.mediaMetadataCompat.description.extras?.getString(
                            MediaMetadataCompat.METADATA_KEY_DISPLAY_TITLE
                        ) ?: "",
                        track = events.mediaMetadataCompat.description.extras?.getInt(
                            MediaMetadataCompat.METADATA_KEY_TRACK_NUMBER
                        ) ?: 1,
                        isFavorite = false,
                    )
                )
            }
        }
    }


    private fun isMusicPlaying(
        mediaItem: MediaFile,
        isPlaying: Boolean
    ) {
        _isPlaying.postValue(isPlaying)
        viewModelScope.launch {
            useCases.insertMediaFileUseCase(
                mediaItem.copy(
                    isPlaying = isPlaying
                )
            )
            while (isPlaying) {
                currentPosition.postValue(exoPlayer.currentPosition)
                delay(500)
            }
        }

    }


    private fun skipToNextSong() {
        musicServiceConnection.transportControls.skipToNext()
    }

    private fun skipToPreviousSong() {
        musicServiceConnection.transportControls.skipToPrevious()
    }

    private fun seekTo(pos: Long) {
        musicServiceConnection.transportControls.seekTo(pos)
    }

    override fun onCleared() {
        super.onCleared()
        musicServiceConnection.unsubscribe(
            MEDIA_ROOT_ID,
            object : MediaBrowserCompat.SubscriptionCallback() {})
    }


}


//        val list = _mediaItems.value
//        val newList = list?.data?.map {
//            if (it.audioFileId == mediaItem.audioFileId){
//                it.copy(
//                    isPlaying = isPlaying
//                )
//            }else{
//                it
//            }
//        }
//        _mediaItems.postValue(
//            Resource.success(
//                newList
//            )
//        )
