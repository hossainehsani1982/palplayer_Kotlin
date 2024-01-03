package com.hossainehs.palplayer.domain.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "subcategory_table")
@Parcelize
data class SubCategory (
    @PrimaryKey(autoGenerate = true) val subCategoryId: Int? = null,
    val mainCategoryName: String = "Home",
    val mainCategoryNumber: Int = 0,
    val name: String = "",
    val filesCount : Int = 0,

) : Parcelable