package com.hossainehs.palplayer.player_service

import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class AudioServicePlaybackHandler @Inject constructor(
    private val exoPlayer: ExoPlayer,
) : Player.Listener {
    private val _audioState: MutableStateFlow<AppAudioState> =
        MutableStateFlow(AppAudioState.Initializing)
    val audioState: StateFlow<AppAudioState> = _audioState.asStateFlow()

    private var job: Job? = null

    init {
        exoPlayer.addListener(this)
    }

    fun addMediaItem(mediaItem: MediaItem) {
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
    }

    fun setMediaItemList(mediaItems: List<MediaItem>) {
        exoPlayer.setMediaItems(mediaItems)
        exoPlayer.prepare()
    }

    suspend fun onPlayerEvents(
        playerEvent: AppPlayerEvents,
        selectedAudioIndex: Int = -1,
        seekPosition: Long = 0,
    ) {
        when (playerEvent) {
            AppPlayerEvents.Backward -> exoPlayer.seekBack()
            AppPlayerEvents.Forward -> exoPlayer.seekForward()
            AppPlayerEvents.SeekToNext -> exoPlayer.seekToNext()
            AppPlayerEvents.SeekToPrevious -> exoPlayer.seekToPrevious()
            AppPlayerEvents.PlayPause -> playOrPause()
            AppPlayerEvents.SeekTo -> exoPlayer.seekTo(seekPosition)
            AppPlayerEvents.SelectedAudioChanged -> {
                when (selectedAudioIndex) {
                    exoPlayer.currentMediaItemIndex -> {
                        playOrPause()
                    }

                    else -> {
                        exoPlayer.seekToDefaultPosition(selectedAudioIndex)
                        _audioState.value = AppAudioState.Playing(
                            isPlaying = true
                        )
                        exoPlayer.playWhenReady = true
                        startProgressUpdate()
                    }
                }
            }

            AppPlayerEvents.Stop -> stopProgressUpdate()
            is AppPlayerEvents.UpdateProgress -> {
                exoPlayer.seekTo(
                    (exoPlayer.duration * playerEvent.newProgress).toLong()
                )
            }
        }
    }

    override fun onPlaybackStateChanged(playbackState: Int) {
        when (playbackState) {
            ExoPlayer.STATE_BUFFERING -> _audioState.value =
                AppAudioState.Buffering(exoPlayer.currentPosition)

            ExoPlayer.STATE_READY -> _audioState.value =
                AppAudioState.Ready(exoPlayer.duration)
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onIsPlayingChanged(isPlaying: Boolean) {
        _audioState.value = AppAudioState.Playing(isPlaying = isPlaying)
        _audioState.value = AppAudioState.CurrentlyPlaying(exoPlayer.currentMediaItemIndex)
        if (isPlaying) {
            GlobalScope.launch(Dispatchers.Main) {
                startProgressUpdate()
            }
        } else {
            stopProgressUpdate()
        }
    }

    private suspend fun playOrPause() {
        if (exoPlayer.isPlaying) {
            exoPlayer.pause()
            stopProgressUpdate()
        } else {
            exoPlayer.play()
            _audioState.value = AppAudioState.Playing(
                isPlaying = true
            )
            startProgressUpdate()
        }
    }

    private suspend fun startProgressUpdate() = job.run {
        while (true) {
            delay(500)
            _audioState.value = AppAudioState.Progress(exoPlayer.currentPosition)
        }
    }

    private fun stopProgressUpdate() {
        job?.cancel()
        _audioState.value = AppAudioState.Playing(isPlaying = false)
    }


}


