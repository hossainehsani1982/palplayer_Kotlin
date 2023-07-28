package com.hossainehs.palplayer.domain.model

import android.net.Uri
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "mediafiles_table")
@Parcelize
data class MediaFile(
    @PrimaryKey(autoGenerate = true) val audioFileId: Int? = null,
    val subCategoryId: Int = 0,
    val path: String = "",
    val relativePath: String = "",
    val artWorkUri: String = "",
    val duration: Long = 0,
    var isPlaying: Boolean = false,
    val album: String = "",
    val artist: String = "",
    val displayName: String = "",
    val track: Int = 0,
    val isFavorite: Boolean = false,
) : Parcelable