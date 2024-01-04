package com.hossainehs.palplayer.domain.use_case

data class UseCases(
    val createNewCategoryUseCase: CreateNewCategoryUseCase,
    val getSubCategoriesWithMediaFilesUseCase: GetSubCategoriesWithMediaFilesUseCase,
    val getSubCategoryWithMediaFilesUseCase: GetSubCategoryWithMediaFilesUseCase,
    val getSystemMediaFilesUseCase: GetSystemMediaFilesUseCase,
    val insertMediaFileUseCase: InsertMediaFileUseCase,
    val getMediaFilesUseCase: GetMediaFilesUseCase,
    val getMediaFileByUri: GetMediaFileByUri,
)
