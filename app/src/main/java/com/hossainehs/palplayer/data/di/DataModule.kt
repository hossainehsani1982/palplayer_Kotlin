package com.hossainehs.palplayer.data.di

import android.app.Application
import android.content.ContentResolver
import android.content.Context
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import androidx.media3.session.MediaSession
import androidx.room.Room
import com.hossainehs.palplayer.data.local.ApplicationDatabase
import com.hossainehs.palplayer.data.repository.RepositoryImpl
import com.hossainehs.palplayer.domain.sharedPreferences.Preferences
import com.hossainehs.palplayer.data.sharedPreferences.PreferencesDataStore
import com.hossainehs.palplayer.data.sharedPreferences.PreferencesImpl
import com.hossainehs.palplayer.domain.repository.Repository
import com.hossainehs.palplayer.domain.use_case.CreateNewCategoryUseCase
import com.hossainehs.palplayer.domain.use_case.GetMediaFileByUri
import com.hossainehs.palplayer.domain.use_case.GetMediaFilesUseCase
import com.hossainehs.palplayer.domain.use_case.GetSubCategoriesWithMediaFilesUseCase
import com.hossainehs.palplayer.domain.use_case.GetSubCategoryWithMediaFilesUseCase
import com.hossainehs.palplayer.domain.use_case.GetSystemMediaFilesUseCase
import com.hossainehs.palplayer.domain.use_case.InsertMediaFileUseCase
import com.hossainehs.palplayer.domain.use_case.UseCases
import com.hossainehs.palplayer.media_item_service.AppMediaSource
import com.hossainehs.palplayer.media_item_service.MediaBrowserConnection
import com.hossainehs.palplayer.player_service.AppNotificationManager
import com.hossainehs.palplayer.player_service.AudioServicePlaybackHandler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideRepository(
        applicationDatabase: ApplicationDatabase,
        contentResolver: ContentResolver
    ): Repository {
        return RepositoryImpl(
            dao = applicationDatabase.dao,
            contentResolver
        )
    }

    @Provides
    @Singleton
    fun provideContentResolver(
        app: Application
    ): ContentResolver {
        return app.contentResolver
    }


    @Provides
    @Singleton
    fun provideApplicationDataBase(
        app: Application
    ): ApplicationDatabase {
        return Room.databaseBuilder(
            app,
            ApplicationDatabase::class.java,
            "palplayer_db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }


    @Provides
    @Singleton
    fun providesPreferencesDataStore(
        app: Application
    ): PreferencesDataStore {
        return PreferencesDataStore(app.applicationContext)
    }

    @Provides
    @Singleton
    fun providesPreferences(preferencesDataStore: PreferencesDataStore): Preferences {
        return PreferencesImpl(preferencesDataStore)
    }

    @Provides
    @Singleton
    fun provideAudioAttributes(): AudioAttributes = AudioAttributes.Builder()
        .setContentType(C.AUDIO_CONTENT_TYPE_MOVIE)
        .setUsage(C.USAGE_MEDIA)
        .build()

    @UnstableApi
    @Provides
    @Singleton
    fun provideExoPlayer(
        @ApplicationContext context: Context,
        audioAttributes: AudioAttributes
    ): ExoPlayer = ExoPlayer.Builder(context)
        .setAudioAttributes(audioAttributes, true)
        .setTrackSelector(DefaultTrackSelector(context))
        .setHandleAudioBecomingNoisy(true)
        .build()

    @Provides
    @Singleton
    fun provideMediaSession(
        @ApplicationContext context: Context,
        player: ExoPlayer
    ): MediaSession = MediaSession.Builder(
        context,
        player
    ).build()

    @Provides
    @Singleton
    fun provideNotificationManager(
        @ApplicationContext context: Context,
        player: ExoPlayer
    ): AppNotificationManager = AppNotificationManager(
        context,
        player
    )

    @Provides
    @Singleton
    fun provideAudioServiceHandler(
        player: ExoPlayer
    ): AudioServicePlaybackHandler = AudioServicePlaybackHandler(
        player
    )

    @Provides
    @Singleton
    fun provideMediaBrowserConnection(
        @ApplicationContext context: Context,
    ): MediaBrowserConnection = MediaBrowserConnection(
        context,
    )

    @Provides
    @Singleton
    fun provideUseCase(
        repository: Repository,
    ): UseCases {
        return UseCases(
            createNewCategoryUseCase = CreateNewCategoryUseCase(repository),
            getSubCategoriesWithMediaFilesUseCase = GetSubCategoriesWithMediaFilesUseCase(repository),
            getSubCategoryWithMediaFilesUseCase = GetSubCategoryWithMediaFilesUseCase(repository),
            getSystemMediaFilesUseCase = GetSystemMediaFilesUseCase(repository),
            insertMediaFileUseCase = InsertMediaFileUseCase(repository),
            getMediaFilesUseCase = GetMediaFilesUseCase(repository),
            getMediaFileByUri = GetMediaFileByUri(repository),
        )
    }

    @Provides
    @Singleton
    fun provideMediaSource(
        useCases: UseCases
    ): AppMediaSource = AppMediaSource(
        useCases
    )



}