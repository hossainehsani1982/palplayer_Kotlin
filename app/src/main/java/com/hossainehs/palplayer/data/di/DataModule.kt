package com.hossainehs.palplayer.data.di

import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.media.AudioAttributes
import android.media.session.MediaSession
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.room.Room
import com.google.android.exoplayer2.ExoPlayer
import com.google.common.util.concurrent.ListenableFuture
import com.hossainehs.palplayer.data.local.ApplicationDatabase
import com.hossainehs.palplayer.data.repository.RepositoryImpl
import com.hossainehs.palplayer.domain.sharedPreferences.Preferences
import com.hossainehs.palplayer.data.sharedPreferences.PreferencesDataStore
import com.hossainehs.palplayer.data.sharedPreferences.PreferencesImpl
import com.hossainehs.palplayer.domain.repository.Repository
import com.hossainehs.palplayer.domain.use_case.CreateMediaSourcesUseCase
import com.hossainehs.palplayer.domain.use_case.CreateNewCategoryUseCase
import com.hossainehs.palplayer.domain.use_case.GetMediaFileByUri
import com.hossainehs.palplayer.domain.use_case.GetSubCategoriesWithMediaFilesUseCase
import com.hossainehs.palplayer.domain.use_case.GetSubCategoryWithMediaFilesUseCase
import com.hossainehs.palplayer.domain.use_case.InsertMediaFileUseCase
import com.hossainehs.palplayer.domain.use_case.UseCases
import com.hossainehs.palplayer.service.MusicServiceConnection
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
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

    @Singleton
    @Provides
    fun providesPlaybackStateBuilder()
            : PlaybackStateCompat.Builder {
        val playbackStateBuilder = PlaybackStateCompat.Builder()
        playbackStateBuilder.setActions(
            PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH or
                    PlaybackStateCompat.ACTION_PREPARE or
                    PlaybackStateCompat.ACTION_PLAY or
                    PlaybackStateCompat.ACTION_PAUSE or
                    PlaybackStateCompat.ACTION_PLAY_PAUSE or
                    PlaybackStateCompat.ACTION_SKIP_TO_NEXT or
                    PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS
        )
        return playbackStateBuilder
    }

    @Singleton
    @Provides
    fun provideAudioAttributes(): AudioAttributes {
        return AudioAttributes.Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .build()
    }
    @Singleton
    @Provides
    fun provideExoPlayer(
        @ApplicationContext context: Context,
        audioAttributes: AudioAttributes
    ): ExoPlayer {
        return ExoPlayer.Builder(context).build()
    }

    @Singleton
    @Provides
    fun provideMediaSessionCompat(
        @ApplicationContext context: Context,
        exoPlayer: ExoPlayer
    ): MediaSessionCompat {
        return MediaSessionCompat(
            context,
            "PalPlayerMediaSession"
        )
    }


    @Provides
    @Singleton
    fun provideMusicServiceConnection(
        @ApplicationContext context: Context,
    ): MusicServiceConnection {
        return MusicServiceConnection(
            context
        )
    }




}