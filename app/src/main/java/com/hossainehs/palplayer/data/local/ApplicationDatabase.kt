package com.hossainehs.palplayer.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hossainehs.palplayer.domain.model.MediaFile
import com.hossainehs.palplayer.domain.model.SubCategory

@Database(
    entities = [
        SubCategory::class,
        MediaFile::class,
    ],
    version = 3,
    exportSchema = false
)
abstract class ApplicationDatabase : RoomDatabase() {
    abstract val dao: AppDao
}