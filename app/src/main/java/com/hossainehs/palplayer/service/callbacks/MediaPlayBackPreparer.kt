package com.hossainehs.palplayer.service.callbacks

import android.media.AudioManager
import android.net.Uri
import android.os.Bundle
import android.os.ResultReceiver
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.PlaybackStateCompat
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.hossainehs.palplayer.service.MusicSource


class MediaPlayBackPreparer(
    private val musicSource: MusicSource,
    private val playerPrepared: (MediaMetadataCompat?) -> Unit
) :
    MediaSessionConnector.PlaybackPreparer,
    AudioManager.OnAudioFocusChangeListener {
    override fun onCommand(
        player: Player,
        command: String,
        extras: Bundle?,
        cb: ResultReceiver?
    ) = false

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