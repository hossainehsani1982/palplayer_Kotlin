package com.hossainehs.palplayer.media_item_service

import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import androidx.media.MediaBrowserServiceCompat
import com.hossainehs.palplayer.data.util.ConstValues.PLAYLIST_AUDIO_BOOKS_ROOT_ID
import com.hossainehs.palplayer.data.util.ConstValues.PLAYLIST_MUSIC_ROOT_ID
import javax.inject.Inject

class MediaBrowserService : MediaBrowserServiceCompat() {

    @Inject
    lateinit var appMediaSource: AppMediaSource

    override fun onCreate() {
        super.onCreate()
        println("MediaBrowserService, onCreate")
    }

    override fun onGetRoot(
        clientPackageName: String,
        clientUid: Int,
        rootHints: Bundle?
    ): BrowserRoot {
        return BrowserRoot(
            PLAYLIST_MUSIC_ROOT_ID,
            null
        )
    }

    override fun onLoadChildren(
        parentId: String,
        result: Result<MutableList<MediaBrowserCompat.MediaItem>>
    ) {
        when (parentId) {
            PLAYLIST_MUSIC_ROOT_ID -> {
                println("MediaBrowserService, onLoadChildren, PLAYLIST_MUSIC_ROOT_ID")
                val resultsSent = appMediaSource.whenReady { isInitialized ->
                    if (isInitialized) {
                        result.sendResult(appMediaSource.subCategoriesAsMediaItem())
                    } else {
                        result.sendResult(null)
                    }
                }
                if (!resultsSent) {
                    result.detach()
                }
            }


            PLAYLIST_AUDIO_BOOKS_ROOT_ID -> {
                val resultsSent = appMediaSource.whenReady { isInitialized ->
                    if (isInitialized) {
                        result.sendResult(appMediaSource.subCategoriesAsMediaItem())
                    } else {
                        result.sendResult(null)
                    }
                }
                if (!resultsSent) {
                    result.detach()
                }
            }
        }
    }


}