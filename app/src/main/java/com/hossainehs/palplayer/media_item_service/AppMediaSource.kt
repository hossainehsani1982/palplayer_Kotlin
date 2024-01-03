package com.hossainehs.palplayer.media_item_service


import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import com.hossainehs.palplayer.domain.use_case.UseCases

class AppMediaSource (
    private val useCases: UseCases
){

    private var subCategories = emptyList<MediaMetadataCompat>()

    enum class State {
        STATE_CREATED,
        STATE_INITIALIZING,
        STATE_INITIALIZED,
        STATE_ERROR
    }

    private val onReadyListeners = mutableListOf<(Boolean) -> Unit>()

    private var state = State.STATE_CREATED
        set(value) {
            if (value == State.STATE_INITIALIZED || value == State.STATE_ERROR) {
                //anything happens inside this block should become from a a same thread
                synchronized(onReadyListeners) {
                    field = value
                    onReadyListeners.forEach { listener ->
                        listener(state == State.STATE_INITIALIZED)
                    }
                }
            } else {
                field = value
            }
        }

    fun whenReady(action: (Boolean) -> Unit): Boolean {
        return if (state == State.STATE_CREATED || state == State.STATE_INITIALIZING) {
            onReadyListeners += action
            false
        } else {
            action(state == State.STATE_INITIALIZED)
            true
        }
    }

//    suspend fun fetchSubcategoriesAsMediaData(
//        mainCatName : String,
//        mainCatId : Int ) = withContext(Dispatchers.IO) {
//        state = State.STATE_INITIALIZING
//        val allSongs : List<SubCategoryWithMediaFile> = useCases.
//        getSubCategoriesWithMediaFilesUseCase(
//            mainCategoryId = mainCatId,
//            mainCategoryName = mainCatName
//        )
//        subCategories = allSongs.map {
//            MediaMetadataCompat.Builder()
//                .putString(
//                    METADATA_KEY_MEDIA_ID
//                    , it.subCategory.mainCategoryNumber.toString()
//                )
//                .build()
//        }
//        state = State.STATE_INITIALIZED
//    }

    fun subCategoriesAsMediaItem(): MutableList<MediaBrowserCompat.MediaItem> {
        println("results: ${subCategories.size}")
        return subCategories.map { subCats ->
            println(
                "song.description.mediaId ${subCats.description.mediaId}" +
                        " ${subCats.description.title}"
            )
            val description = MediaDescriptionCompat.Builder()
                .setMediaId(
                    subCats.description.mediaId
                )
                .setTitle(
                    subCats.description.title
                )
                .build()
            //it can be a song or anything else
            MediaBrowserCompat.MediaItem(
                description,
                MediaBrowserCompat.MediaItem.FLAG_PLAYABLE
            )
        }.toMutableList()
    }






}