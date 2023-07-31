package com.hossainehs.palplayer.domain.repository

import android.content.ContentResolver
import androidx.lifecycle.LiveData
import com.hossainehs.palplayer.domain.model.Relation.SubCategoryWithMediaFile
import com.hossainehs.palplayer.domain.model.MediaFile
import com.hossainehs.palplayer.domain.model.SubCategory
import com.hossainehs.palplayer.domain.model.SystemMediaFile
import kotlinx.coroutines.flow.Flow

interface Repository {



    suspend fun insertNewSubCategory(subCategory: SubCategory)

    fun getSubCategoriesWithMediaFilesByMainCategoryName(
        mainCategoryId: Int,
        mainCategoryName: String
    ): Flow<List<SubCategoryWithMediaFile>>


   suspend fun getSubCategoryWithMediaFilesBySubCategoryId(
        subCategoryId: Int
    ): SubCategoryWithMediaFile

    suspend fun insertNewMediaFile(mediaFile: MediaFile)

    fun getMediaFilesBySubCategoryId(
        subCategoryId: Int
    ): Flow<List<MediaFile>>

    suspend fun getMediaFilesFromDevice(
        subCategory: SubCategory
    ): Flow<List<SystemMediaFile>>

    suspend fun getMediaFileByUri(
       uri : String
    ): MediaFile


}