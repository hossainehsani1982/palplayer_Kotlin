package com.hossainehs.palplayer.service


import android.app.PendingIntent
import android.content.Intent
import android.media.MediaMetadata
import android.os.Binder
import android.os.Bundle
import android.os.IBinder
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.media.MediaBrowserServiceCompat
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.ext.mediasession.TimelineQueueNavigator
import com.google.android.exoplayer2.upstream.DefaultDataSource
import com.hossainehs.palplayer.data.util.ConstValues.DATABASE_ERROR
import com.hossainehs.palplayer.data.util.ConstValues.MEDIA_ROOT_ID
import com.hossainehs.palplayer.data.util.ConstValues.PLAYLIST_ROOT_ID
import com.hossainehs.palplayer.domain.sharedPreferences.Preferences
import com.hossainehs.palplayer.domain.use_case.GetMediaFileByUri
import com.hossainehs.palplayer.service.callbacks.ExoPlayerListener
import com.hossainehs.palplayer.service.callbacks.MusicPlayerNotificationListener
import com.hossainehs.palplayer.service.callbacks.MediaPlayBackPreparer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.internal.notify
import javax.inject.Inject

@AndroidEntryPoint
class MusicService : MediaBrowserServiceCompat() {

    private lateinit var mediaSessionPreparer: MediaPlayBackPreparer
    private lateinit var exoPlayerListener: ExoPlayerListener
    private lateinit var notificationManager: MusicNotificationManager
    private lateinit var mediaSessionConnector: MediaSessionConnector
    private var mediaBrowserCompatMediaItems: MutableList<MediaBrowserCompat.MediaItem>? = null
    private var currentPlayingSong: MediaMetadataCompat? = null
    // private val palPlayerBinder = PalPlayerBinder()

    var isForegroundService = false

    private var isPlayerInitialized = false
    private val serviceJobs = Job()
    private val serviceScope = CoroutineScope(
        Dispatchers.Main + serviceJobs
    )


    @Inject
    lateinit var exoPlayer: ExoPlayer

    @Inject
    lateinit var mediaSessionCompat: MediaSessionCompat

    @Inject
    lateinit var playbackStateBuilder: PlaybackStateCompat.Builder


    @Inject
    lateinit var preferences: Preferences

    @Inject
    lateinit var getMediaFileByUri: GetMediaFileByUri

    @Inject
    lateinit var musicSource: MusicSource

    @Inject
    lateinit var datasourceFactory: DefaultDataSource.Factory


    companion object {
        var currentSongDuration = 0L
            private set
    }


    override fun onCreate() {
        super.onCreate()

        musicSource.fetchMediaData(1)
        musicSource.fetchSubCategoriesMediaData(1, "music")

        val activityIntent = packageManager?.getLaunchIntentForPackage(packageName)?.let {
            PendingIntent.getActivity(this, 0, it, PendingIntent.FLAG_IMMUTABLE)
        }

        mediaSessionCompat.apply {
            setSessionActivity(activityIntent)
            isActive = true
        }

        exoPlayerListener = ExoPlayerListener(
            musicService = this@MusicService
        )

        notificationManager = MusicNotificationManager(
            context = this,
            exoPlayer = exoPlayer,
            sessionToken = mediaSessionCompat.sessionToken,
            notificationListener = MusicPlayerNotificationListener(
                this@MusicService
            )
        ) {
            currentSongDuration = exoPlayer.duration
            exoPlayer.mediaMetadata.artist
            //when the user click on the notification, we will navigate to the player fragment.
        }

        mediaSessionPreparer = MediaPlayBackPreparer(
            musicSource = musicSource,
            serviceScope = serviceScope,
            musicService = this@MusicService
        ) { mediaMetaData ->

            currentPlayingSong = mediaMetaData

            preparePlayer(
                musicSource.songs,
                mediaMetaData,
                true
            )
        }

        sessionToken = mediaSessionCompat.sessionToken
        mediaSessionConnector = MediaSessionConnector(mediaSessionCompat)
        mediaSessionConnector.setPlaybackPreparer(mediaSessionPreparer)
        mediaSessionConnector.setQueueNavigator(MusicQueueNavigator())
        mediaSessionConnector.setPlayer(exoPlayer)

        exoPlayerListener = ExoPlayerListener(this)
        exoPlayer.addListener(exoPlayerListener)


    }

    private inner class MusicQueueNavigator : TimelineQueueNavigator(mediaSessionCompat) {
        override fun getMediaDescription(player: Player, windowIndex: Int): MediaDescriptionCompat {
            return musicSource.songs[windowIndex].description
        }
    }

    private fun preparePlayer(
        songs: List<MediaMetadataCompat>,
        itemToPlay: MediaMetadataCompat?,
        playNow: Boolean
    ) {
        val curSongIndex = if (currentPlayingSong == null) 0 else songs.indexOf(itemToPlay)

        exoPlayer.prepare(musicSource.asMediaSource(datasourceFactory))
        exoPlayer.seekTo(curSongIndex, 0L)
        exoPlayer.playWhenReady = playNow

    }

    override fun onGetRoot(
        clientPackageName: String,
        clientUid: Int,
        rootHints: Bundle?
    ): BrowserRoot? {
        return BrowserRoot(
            PLAYLIST_ROOT_ID,
            null
        )

    }
    var currentResult: Result<List<MediaBrowserCompat.MediaItem>>? = null

    override fun onLoadChildren(
        parentId: String,
        result: Result<MutableList<MediaBrowserCompat.MediaItem>>
    ) {
        when (parentId) {
            PLAYLIST_ROOT_ID -> {
                try {
                    result.detach()
                }catch (e: Exception){
                    println("onLoadChildren exception: ${e.message}")
                }
                val resultsSent = musicSource.whenReady { isInitialized ->
                    if (isInitialized) {
                            try {
                                var results = musicSource.asMediaItem()

                                result.sendResult(results)
                                if (!isPlayerInitialized && musicSource.subCategories.isNotEmpty()) {
                                    isPlayerInitialized = true
                                }

                            } catch (e: Exception) {

                            }

                    } else {
                        mediaSessionCompat.sendSessionEvent(
                            DATABASE_ERROR,
                            null
                        )
                        result.sendResult(null)
                    }
                }
                if (!resultsSent) {
                    try {
                        result.detach()
                    }catch (e: Exception){
                        println("onLoadChildren exception: ${e.message}")
                    }
                }

            }

            MEDIA_ROOT_ID -> {
                println("MEDIA_ROOT_ID connected")
                musicSource.whenReady { isInitialized ->
                    if (isInitialized) {
                        result.sendResult(musicSource.asMediaItem())
                        if (!isPlayerInitialized && musicSource.songs.isNotEmpty()) {
                            preparePlayer(
                                musicSource.songs,
                                musicSource.songs[0],
                                false
                            )
                            isPlayerInitialized = true
                        }

                    } else {
                        mediaSessionCompat.sendSessionEvent(
                            DATABASE_ERROR,
                            null
                        )
                        result.sendResult(null)
                    }
                }

            }
        }
    }


//    inner class PalPlayerBinder : Binder() {
//
//        fun getService(): MusicService {
//            return this@MusicService
//        }
//    }
//
//    override fun onBind(intent: Intent?): IBinder? {
//        super.onBind(intent)
//        return palPlayerBinder
//    }


    override fun onDestroy() {
        super.onDestroy()
        serviceJobs.cancel()
        println("destroyed")
    }


}


// Enable callbacks from MediaButtons and TransportControls
//            setFlags(
//                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS
//                        or MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS
//                        or MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS
//            )


//        musics = mediaSources.map {
//            MediaBrowserCompat.MediaItem(
//                it.mediaItem.mediaMetadata. as MediaDescriptionCompat,
//                MediaBrowserCompat.MediaItem.FLAG_PLAYABLE
//            )
//        }.toMutableList()


//var shareFlowIsAppPlaying = _shareFlowIsAppPlaying.asSharedFlow()
//private var _shareFlowIsFilePlaying =  MutableSharedFlow<Boolean>()
//var shareFlowIsFilePlaying = _shareFlowIsFilePlaying.asSharedFlow()
//private var _shareFlowFileUri =  MutableSharedFlow<String>()
//var shareFlowFileUri = _shareFlowFileUri.asSharedFlow()
