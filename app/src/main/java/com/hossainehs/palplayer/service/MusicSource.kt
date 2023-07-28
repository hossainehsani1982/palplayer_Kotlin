package com.hossainehs.palplayer.service

import android.content.Context
import android.media.MediaMetadata
import android.net.Uri
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import androidx.core.net.toUri
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSource
import com.hossainehs.palplayer.domain.model.MediaFile
import com.hossainehs.palplayer.domain.model.SubCategory
import com.hossainehs.palplayer.domain.use_case.CreateMediaMetaDataCompatUseCase
import com.hossainehs.palplayer.domain.use_case.CreateMediaSourcesUseCase
import com.hossainehs.palplayer.domain.use_case.GetMediaFilesUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext


/*
* inside this class we will get the list of songs from firebase.
* it take some time to get the list, but it is hard in our service to wait until it is finished.
* so we use state variable to get the state of the music source.
* before downloading music from firebase, the state is STATE_INITIALIZING.
* when it is finished, the state is STATE_INITIALIZED.
* if error happens while downloading musics, the state is STATE_ERROR.
* so we use state to schedule an action when the source is finished loading.
*when it is finished, we will add the action to onReadyListeners list.
* and when the source is ready, we will call the action function.
*/

class MusicSource(
    private val createMediaSourcesUseCase: CreateMediaSourcesUseCase,
    private val getMediaFilesUseCase: GetMediaFilesUseCase,
) {

    var songs = emptyList<MediaMetadataCompat>()

    var mediaFileList: List<MediaFile> = listOf()

    var subFolders = emptyList<SubCategory>()

    private val onReadyListeners = mutableListOf<(Boolean) -> Unit>()
    private var state = State.STATE_CREATED
        set(value) {
            if (value == State.STATE_INITIALIZED || value == State.STATE_ERROR) {
                //anything happens inside this block should become from a a same thread
                synchronized(onReadyListeners) {
                    field = value
                    onReadyListeners.forEach { listener ->
                        listener(state == State.STATE_INITIALIZED)
                    }
                }
            } else {
                field = value
            }
        }

    fun fetchMediaData(
        subCategoryId: Int
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            state = State.STATE_INITIALIZING
            getMediaFilesUseCase(
                subCategoryId
            ).collectLatest { mediaFiles ->
                songs = mediaFiles.map { mediaFile ->
                    MediaMetadataCompat.Builder()
                        .putString(
                            MediaMetadataCompat.METADATA_KEY_MEDIA_ID,
                            mediaFile.audioFileId.toString()
                        )
                        .putString(
                            MediaMetadataCompat.METADATA_KEY_MEDIA_URI,
                            mediaFile.path
                        )
                        .putString(
                            MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON_URI,
                            mediaFile.artWorkUri
                        )
                        .putString(
                            MediaMetadataCompat.METADATA_KEY_ALBUM,
                            mediaFile.album
                        )
                        .putString(
                            MediaMetadataCompat.METADATA_KEY_ARTIST,
                            mediaFile.artist
                        )
                        .putString(
                            MediaMetadataCompat.METADATA_KEY_TITLE,
                            mediaFile.displayName.replace(".mp3", "")
                        )
                        .putLong(
                            MediaMetadataCompat.METADATA_KEY_DURATION,
                            mediaFile.duration
                        )
                        .build()
                }
                state = State.STATE_INITIALIZED
            }
        }

    }


    fun whenReady(action: (Boolean) -> Unit): Boolean {
        return if (state == State.STATE_CREATED || state == State.STATE_INITIALIZING) {
            onReadyListeners += action
            false
        } else {
            action(state == State.STATE_INITIALIZED)
            true
        }
    }


    fun asMediaSource(dataSourceFactory: DefaultDataSource.Factory): Flow<ConcatenatingMediaSource> {
        val concatenatingMediaSource = ConcatenatingMediaSource()
        val mutableListMediaSource = mutableListOf<MediaSource>()
        songs.forEach { mediaFile ->
            val mediaSource = createMediaSourcesUseCase(
                mediaFile,
                dataSourceFactory
            )
            concatenatingMediaSource.addMediaSource(mediaSource)
        }
        return flow { emit(concatenatingMediaSource) }
    }

    fun asMediaItem(): Flow<MutableList<MediaBrowserCompat.MediaItem>> {
        return flow { emit( songs.map { song ->
            val description = MediaDescriptionCompat.Builder()
                .setMediaUri(
                    song.getString(
                        MediaMetadata.METADATA_KEY_MEDIA_URI
                    )!!.toUri()
                )
                .setTitle(
                    song.description.title
                )
                .setMediaId(
                    song.description.mediaId
                )
                .setIconUri(
                    song.description.iconUri
                )
                .setExtras(
                    Bundle().apply {
                        putString(
                            MediaMetadata.METADATA_KEY_MEDIA_ID,
                            song.getString(
                                MediaMetadata.METADATA_KEY_MEDIA_ID
                            )
                        )
                        putLong(
                            MediaMetadata.METADATA_KEY_DURATION,
                            song.getLong(
                                MediaMetadata.METADATA_KEY_DURATION
                            )
                        )
                        putString(
                            MediaMetadata.METADATA_KEY_DISPLAY_TITLE,
                            song.getString(
                                MediaMetadata.METADATA_KEY_DISPLAY_TITLE
                            )
                        )
                        putString(
                            MediaMetadata.METADATA_KEY_ARTIST,
                            song.getString(
                                MediaMetadata.METADATA_KEY_ARTIST
                            )
                        )
                        putString(
                            MediaMetadata.METADATA_KEY_ALBUM_ART_URI,
                            song.getString(
                                MediaMetadata.METADATA_KEY_ALBUM_ART_URI
                            )
                        )
                        putString(
                            MediaMetadata.METADATA_KEY_ALBUM,
                            song.getString(
                                MediaMetadata.METADATA_KEY_ALBUM
                            )
                        )
                    }
                )
                .build()
            //it can be a song or anything else
            MediaBrowserCompat.MediaItem(
                description,
                MediaBrowserCompat.MediaItem.FLAG_PLAYABLE
            )
        }.toMutableList()) }
    }
}


enum class State {
    STATE_CREATED,
    STATE_INITIALIZING,
    STATE_INITIALIZED,
    STATE_ERROR
}


//songs = mediaFiles.map { mediaFile ->
//    MediaMetadataCompat.Builder()
//        .putString(
//            MediaMetadataCompat.METADATA_KEY_MEDIA_ID,
//            mediaFile.audioFileId.toString()
//        )
//        .putString(
//            MediaMetadataCompat.METADATA_KEY_MEDIA_URI,
//            mediaFile.path
//        )
//        .putString(
//            MediaMetadataCompat.METADATA_KEY_ALBUM,
//            mediaFile.album
//        )
//        .putString(
//            MediaMetadataCompat.METADATA_KEY_ARTIST,
//            mediaFile.artist
//        )
//        .putString(
//            MediaMetadataCompat.METADATA_KEY_TITLE,
//            mediaFile.displayName.replace(".mp3", "")
//        )
//        .build()
//}