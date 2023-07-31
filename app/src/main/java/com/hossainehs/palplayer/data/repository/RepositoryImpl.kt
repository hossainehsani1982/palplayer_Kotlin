package com.hossainehs.palplayer.data.repository

import android.content.ContentResolver
import android.provider.MediaStore
import com.hossainehs.palplayer.data.local.AppDao
import com.hossainehs.palplayer.domain.model.Relation.SubCategoryWithMediaFile
import com.hossainehs.palplayer.domain.model.MediaFile
import com.hossainehs.palplayer.domain.model.SubCategory
import com.hossainehs.palplayer.domain.model.SystemMediaFile
import com.hossainehs.palplayer.domain.repository.Repository
import com.hossainehs.palplayer.presentation.util.sdk29AndUp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RepositoryImpl(
    private val dao: AppDao,
    private val contentResolver: ContentResolver
) : Repository {

    override suspend fun insertNewSubCategory(
        subCategory: SubCategory
    ) {
        dao.insertSubCategory(
            subCategory
        )

    }

    override fun getSubCategoriesWithMediaFilesByMainCategoryName(
        mainCategoryId: Int,
        mainCategoryName: String
    ): Flow<List<SubCategoryWithMediaFile>> {
        return dao.getSubCategoriesWithMediaFiles(
            mainCategoryId,
            mainCategoryName
        )
    }


    override suspend fun getSubCategoryWithMediaFilesBySubCategoryId(
        subCategoryId: Int
    ): SubCategoryWithMediaFile {
        return dao.getSubCategoryWithMediaFiles(
            subCategoryId
        )
    }

    override suspend fun insertNewMediaFile(mediaFile: MediaFile) {
        dao.insertMediaFile(
            mediaFile
        )
    }

    override fun getMediaFilesBySubCategoryId(
        subCategoryId: Int
    ): Flow<List<MediaFile>> {
        return dao.getMediaFiles(
            subCategoryId
        )
    }

    override suspend fun getMediaFilesFromDevice(
        subCategory: SubCategory
    ): Flow<List<SystemMediaFile>> {
        val musics = mutableListOf<SystemMediaFile>()
        return flow {
            val collection = sdk29AndUp {
                MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
            } ?: MediaStore.Audio.Media.EXTERNAL_CONTENT_URI

            val selection = MediaStore.Audio.Media.IS_MUSIC

            val projection = arrayOf(
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.RELATIVE_PATH,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.TRACK,
                MediaStore.Audio.Albums.ALBUM_ID
            )

            contentResolver.query(
                collection,
                projection,
                selection,
                null,
                "${MediaStore.Audio.Media.DISPLAY_NAME} ASC"
            )?.use { cursor ->


                val idColumn = cursor.getColumnIndexOrThrow(
                    MediaStore.Audio.Media._ID
                )

                val fRelativePath = cursor.getColumnIndexOrThrow(
                    MediaStore.Audio.Media.RELATIVE_PATH
                )

                val displayNameColumn = cursor.getColumnIndexOrThrow(
                    MediaStore.Audio.Media.DISPLAY_NAME
                )

                val fTrack = cursor.getColumnIndexOrThrow(
                    MediaStore.Audio.Media.TRACK
                )

                val artistColumn = cursor.getColumnIndexOrThrow(
                    MediaStore.Audio.Media.ARTIST
                )
                val durationColumn = cursor.getColumnIndexOrThrow(
                    MediaStore.Audio.Media.DURATION
                )
                val albumColumn = cursor.getColumnIndexOrThrow(
                    MediaStore.Audio.Albums.ALBUM_ID
                )

                println(cursor.count)
                while (cursor.moveToNext()) {
                    val audioID = cursor.getLong(idColumn)
                    val path = audioID.toString()
                    val relativePath = cursor.getString(fRelativePath)
                    val displayName = cursor.getString(displayNameColumn)
                    val artist = cursor.getString(artistColumn)
                    val duration = cursor.getLong(durationColumn)
                    val album = cursor.getString(albumColumn)
                    val track = cursor.getInt(fTrack)
                    val contentUri =
                        "content://media/external/audio/media/$audioID/albumart"


                    musics.add(
                        SystemMediaFile(
                            subCategoryId = subCategory.subCategoryId!!,
                            subCategoryName = subCategory.name,
                            mainCategoryId = subCategory.mainCategoryNumber,
                            mainCategoryName = subCategory.mainCategoryName,
                            fileName = displayName,
                            filePath = contentUri.replace("/albumart", ""),
                            fileRelativePath = relativePath,
                            artist = artist,
                            track = track,
                            duration = duration,
                            album = album,
                            contentUri = contentUri
                        )
                    )

                    //
                    emit(musics.toList())
                }
            }
        }
    }

    override suspend fun getMediaFileByUri(uri: String): MediaFile {
        return dao.getMediaFilesByUri(
            uri
        )
    }

}



