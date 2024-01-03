package com.hossainehs.palplayer.domain.model.relation

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Relation
import com.hossainehs.palplayer.domain.model.MediaFile
import com.hossainehs.palplayer.domain.model.SubCategory
import kotlinx.parcelize.Parcelize

@Parcelize
data class SubCategoryWithMediaFile (
    @Embedded
    val subCategory : SubCategory,

    @Relation(
        parentColumn = "subCategoryId",
        entityColumn = "subCategoryId"
    )
    val mediaFiles : List<MediaFile>
        ) : Parcelable
