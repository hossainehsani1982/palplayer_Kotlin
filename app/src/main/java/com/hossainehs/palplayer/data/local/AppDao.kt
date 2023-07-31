package com.hossainehs.palplayer.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.hossainehs.palplayer.domain.model.Relation.SubCategoryWithMediaFile
import com.hossainehs.palplayer.domain.model.MediaFile
import com.hossainehs.palplayer.domain.model.SubCategory
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {


    @Transaction
    @Query(
        """
        SELECT * FROM subcategory_table
        WHERE mainCategoryNumber = :mainCategoryId
        AND mainCategoryName = :mainCategoryName
        """
    )
    fun getSubCategoriesWithMediaFiles(
        mainCategoryId: Int,
        mainCategoryName: String
    ): Flow<List<SubCategoryWithMediaFile>>

    @Transaction
    @Query(
        """
        SELECT * FROM subcategory_table
        WHERE subCategoryId = :subCategoryId
        """
    )
    suspend fun getSubCategoryWithMediaFiles(
        subCategoryId: Int
    ): SubCategoryWithMediaFile

    @Transaction
    @Query(
        """
        SELECT * FROM mediafiles_table
        WHERE subCategoryId = :subCategoryId
        """
    )
    fun getMediaFiles(
        subCategoryId: Int
    ): Flow<List<MediaFile>>

    @Transaction
    @Query(
        """
        SELECT * FROM mediafiles_table
        WHERE path = :path
        """
    )
    suspend fun getMediaFilesByUri(
        path : String
    ): MediaFile


    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubCategory(subCategoryEntity: SubCategory)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMediaFile(mediaFile: MediaFile)


    @Transaction
    @Delete
    suspend fun deleteSubCategory(subCategoryEntity: SubCategory)

    @Transaction
    @Delete
    suspend fun deleteMediaFile(mediaFile: MediaFile)


}