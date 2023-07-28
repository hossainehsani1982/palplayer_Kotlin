package com.hossainehs.palplayer.domain.di

import com.hossainehs.palplayer.domain.repository.Repository
import com.hossainehs.palplayer.domain.use_case.CreateMediaSourcesUseCase
import com.hossainehs.palplayer.domain.use_case.CreateNewCategoryUseCase
import com.hossainehs.palplayer.domain.use_case.GetMediaFileByUri
import com.hossainehs.palplayer.domain.use_case.GetSubCategoriesWithMediaFilesUseCase
import com.hossainehs.palplayer.domain.use_case.GetSubCategoryWithMediaFilesUseCase
import com.hossainehs.palplayer.domain.use_case.GetSystemMediaFilesUseCase
import com.hossainehs.palplayer.domain.use_case.InsertMediaFileUseCase
import com.hossainehs.palplayer.domain.use_case.UseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Singleton


@Module
@InstallIn(ViewModelComponent::class)
object DomainModule {


    @Provides
    @ViewModelScoped
    fun provideUseCase(
        repository: Repository,
    ): UseCases {
        return UseCases(
            createNewCategoryUseCase = CreateNewCategoryUseCase(repository),
            getSubCategoriesWithMediaFilesUseCase = GetSubCategoriesWithMediaFilesUseCase(repository),
            getSubCategoryWithMediaFilesUseCase = GetSubCategoryWithMediaFilesUseCase(repository),
            getSystemMediaFilesUseCase = GetSystemMediaFilesUseCase(repository),
            insertMediaFileUseCase = InsertMediaFileUseCase(repository),
            createMediaSourcesUseCase = CreateMediaSourcesUseCase(),
            getMediaFileByUri = GetMediaFileByUri(repository),
        )
    }

}