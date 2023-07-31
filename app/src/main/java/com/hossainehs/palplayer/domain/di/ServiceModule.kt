package com.hossainehs.palplayer.domain.di

import android.content.Context
import android.media.AudioAttributes
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSource
import com.hossainehs.palplayer.domain.repository.Repository
import com.hossainehs.palplayer.domain.use_case.CreateMediaMetaDataCompatUseCase
import com.hossainehs.palplayer.domain.use_case.CreateMediaSourcesUseCase
import com.hossainehs.palplayer.domain.use_case.GetMediaFileByUri
import com.hossainehs.palplayer.domain.use_case.GetMediaFilesUseCase
import com.hossainehs.palplayer.domain.use_case.GetSubCategoriesWithMediaFilesUseCase
import com.hossainehs.palplayer.domain.use_case.GetSubCategoryWithMediaFilesUseCase
import com.hossainehs.palplayer.service.MusicServiceConnection
import com.hossainehs.palplayer.service.MusicSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Singleton

@Module
@InstallIn(ServiceComponent::class)
object ServiceModule {


    @Provides
    @ServiceScoped
    fun provideDataSource(
        @ApplicationContext context: Context,
        dataSourceFactory: DefaultDataSource.Factory
    ): DataSource.Factory {
        return DefaultDataSource.Factory(
            context,
            dataSourceFactory
        )
    }

//    @ServiceScoped
//    @Provides
//    fun provideMediaSessionCompat(
//        @ApplicationContext context: Context
//    ) : MediaSessionCompat {
//        return MediaSessionCompat(
//            context,
//            "PalPlayerMediaSession"
//        )
//    }

//    @ServiceScoped
//    @Provides
//    fun providesPlaybackStateBuilder()
//            : PlaybackStateCompat.Builder {
//        val playbackStateBuilder = PlaybackStateCompat.Builder()
//        playbackStateBuilder.setActions(
//            PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH or
//                    PlaybackStateCompat.ACTION_PREPARE or
//                    PlaybackStateCompat.ACTION_PLAY or
//                    PlaybackStateCompat.ACTION_PAUSE or
//                    PlaybackStateCompat.ACTION_PLAY_PAUSE or
//                    PlaybackStateCompat.ACTION_SKIP_TO_NEXT or
//                    PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS
//        )
//        return playbackStateBuilder
//    }

//    @ServiceScoped
//    @Provides
//    fun provideControllerCompat(
//        @ApplicationContext context: Context,
//        mediaSession: MediaSessionCompat
//    ): MediaControllerCompat {
//        return MediaControllerCompat(context, mediaSession.sessionToken)
//    }
//
//    @ServiceScoped
//    @Provides
//    fun provideCompatTransportControls(
//        mediaControllerCompat: MediaControllerCompat
//    ): MediaControllerCompat.TransportControls {
//        return mediaControllerCompat.transportControls
//    }





    @ServiceScoped
    @Provides
    fun provideUseCase(
        repository: Repository
    ): GetMediaFileByUri {
        return GetMediaFileByUri(repository)
    }


    @ServiceScoped
    @Provides
    fun provideDataSourceFactory(
        @ApplicationContext context: Context
    ): DefaultDataSource.Factory {
        return return DefaultDataSource.Factory(
            context
        )
    }


    @ServiceScoped
    @Provides
    fun provideCreateMediaSourceUseCase(
    ): CreateMediaSourcesUseCase {
        return CreateMediaSourcesUseCase()
    }

    @ServiceScoped
    @Provides
    fun provideMediaMetadata(): CreateMediaMetaDataCompatUseCase {
        return CreateMediaMetaDataCompatUseCase()
    }

    @ServiceScoped
    @Provides
    fun provideGetMediaFilesUseCase(
        repository: Repository
    ): GetMediaFilesUseCase {
        return GetMediaFilesUseCase(
            repository
        )
    }

    @ServiceScoped
    @Provides
    fun provideGetSubCategoryWithMediaFilesUseCase(
        repository: Repository
    ): GetSubCategoriesWithMediaFilesUseCase {
        return GetSubCategoriesWithMediaFilesUseCase(
            repository
        )
    }


    @ServiceScoped
    @Provides
    fun provideMusicSource(
        createMediaSourcesUseCase: CreateMediaSourcesUseCase,
        getMediaFilesUseCase: GetMediaFilesUseCase,
        getSubCategoriesWithMediaFilesUseCase: GetSubCategoriesWithMediaFilesUseCase
    ): MusicSource {
        return MusicSource(
            createMediaSourcesUseCase,
            getMediaFilesUseCase,
            getSubCategoriesWithMediaFilesUseCase
        )
    }







}