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
        subCategoryId: Int,
        mainCategoryName: String
    ): Flow<List<SubCategoryWithMediaFile>>


    fun getSubCategoryWithMediaFilesBySubCategoryId(
        subCategoryId: Int
    ): Flow<SubCategoryWithMediaFile>

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