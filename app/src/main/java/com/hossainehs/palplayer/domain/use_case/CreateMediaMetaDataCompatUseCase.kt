package com.hossainehs.palplayer.domain.use_case

import android.content.Context
import android.media.MediaMetadata
import android.provider.Settings.Global.putString
import android.support.v4.media.MediaMetadataCompat
import com.hossainehs.palplayer.domain.model.MediaFile

class CreateMediaMetaDataCompatUseCase {
    operator fun invoke(
        mediaFile: MediaFile,
        context: Context
    ): MediaMetadataCompat {
        val result = MediaMetadataCompat.Builder()
            .putString(
                MediaMetadataCompat.METADATA_KEY_MEDIA_ID,
                mediaFile.audioFileId.toString()
            )
            .putString(
                MediaMetadataCompat.METADATA_KEY_MEDIA_URI,
                mediaFile.path
            )
//            putString(
//                MediaMetadataCompat.METADATA_KEY_DURATION,
//                mediaFile.duration.toString()
//            )
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
//            putString(
//                MediaMetadataCompat.METADATA_KEY_TRACK_NUMBER,
//                mediaFile.track.toString()
//            )
        .build()

        return result
    }
}