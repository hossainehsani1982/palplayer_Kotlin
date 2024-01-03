package com.hossainehs.palplayer.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SystemMediaFile(
    var fileId: Int = 0,
    val subCategoryId: Int = 0,
    val subCategoryName: String = "",
    val mainCategoryId: Int = 0,
    val mainCategoryName: String = "",
    val fileName: String = "",
    val fileRelativePath : String = "",
    val filePath: String = "",
    val artist: String = "",
    val album: String = "",
    val track: Int = 0,
    val contentUri : String? = "",
    val duration: Long = 0,
    var isSelected : Boolean = false

) : Parcelable
