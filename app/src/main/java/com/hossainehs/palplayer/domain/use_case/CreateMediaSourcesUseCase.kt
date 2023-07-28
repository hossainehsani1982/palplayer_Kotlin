package com.hossainehs.palplayer.domain.use_case

import android.media.MediaMetadata
import android.support.v4.media.MediaMetadataCompat
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DataSource


class CreateMediaSourcesUseCase() {
    operator fun invoke(
        mediaMetadataCompat: MediaMetadataCompat,
        dataSourceFactory: DataSource.Factory

    ): ProgressiveMediaSource {

        return ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(
                MediaItem.Builder()
                    .setUri(
                        mediaMetadataCompat.getString(
                            MediaMetadata.METADATA_KEY_MEDIA_URI
                        )
                    )
                    .setMediaId(
                        mediaMetadataCompat.getString(
                            MediaMetadata.METADATA_KEY_MEDIA_ID
                        )!!
                    )
                    .build()
            )
    }
}