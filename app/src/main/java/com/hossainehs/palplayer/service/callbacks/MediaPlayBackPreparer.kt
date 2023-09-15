package com.hossainehs.palplayer.service.callbacks

import android.media.AudioManager
import android.net.Uri
import android.os.Bundle
import android.os.ResultReceiver
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.PlaybackStateCompat
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.hossainehs.palplayer.data.util.ConstValues
import com.hossainehs.palplayer.data.util.ConstValues.ADD_NEW_MEDIA_FILE
import com.hossainehs.palplayer.data.util.ConstValues.ADD_SUB_CATEGORY
import com.hossainehs.palplayer.data.util.ConstValues.GET_MEDIA_FILES_IN_SUBCATEGORY
import com.hossainehs.palplayer.data.util.ConstValues.GET_SUB_CATEGORIES
import com.hossainehs.palplayer.data.util.ConstValues.MAIN_CATEGORY_NAME
import com.hossainehs.palplayer.data.util.ConstValues.MAIN_CATEGORY_NUMBER
import com.hossainehs.palplayer.service.MusicService
import com.hossainehs.palplayer.service.MusicSource
import dagger.hilt.android.scopes.ServiceScoped
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


class MediaPlayBackPreparer(
    private val musicSource: MusicSource,
    private val serviceScope: CoroutineScope,
    private val musicService: MusicService,
    private val playerPrepared: (MediaMetadataCompat?) -> Unit
) :
    MediaSessionConnector.PlaybackPreparer,
    AudioManager.OnAudioFocusChangeListener {
    override fun onCommand(
        player: Player,
        command: String,
        extras: Bundle?,
        cb: ResultReceiver?
    ) : Boolean {
        when(command){
            //edit data or fetch more data from api
            GET_SUB_CATEGORIES->{

                val mainCategoryNumber = extras?.getInt(MAIN_CATEGORY_NUMBER)
                val mainCategoryName = extras?.getString(MAIN_CATEGORY_NAME)
                println("onCommand: GET_SUB_CATEGORIES $mainCategoryName , $mainCategoryNumber")

                serviceScope.launch {
                    musicSource.fetchSubCategoriesMediaData(
                        mainCategoryNumber!!,
                        mainCategoryName!!)
                    musicSource.subCategoriesAsMediaItem()

                    //musicService.notifyChildrenChanged(ConstValues.PLAYLIST_ROOT_ID)
                }

            }
            GET_MEDIA_FILES_IN_SUBCATEGORY ->{

            }
            ADD_NEW_MEDIA_FILE ->{

            }
        }
        return false
    }

    override fun getSupportedPrepareActions(): Long {
        return PlaybackStateCompat.ACTION_PREPARE_FROM_MEDIA_ID or
                PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID
    }

    override fun onPrepare(playWhenReady: Boolean) = Unit

    override fun onPrepareFromMediaId(mediaId: String, playWhenReady: Boolean, extras: Bundle?) {
        musicSource.whenReady {
            val itemToPlay = musicSource.songs.find { mediaId == it.description.mediaId }
            playerPrepared(itemToPlay)
        }
    }


    override fun onPrepareFromSearch(query: String, playWhenReady: Boolean, extras: Bundle?) {
        TODO("Not yet implemented")
    }

    override fun onPrepareFromUri(uri: Uri, playWhenReady: Boolean, extras: Bundle?) {
        TODO("Not yet implemented")
    }

    override fun onAudioFocusChange(p0: Int) {
        TODO("Not yet implemented")
    }


}