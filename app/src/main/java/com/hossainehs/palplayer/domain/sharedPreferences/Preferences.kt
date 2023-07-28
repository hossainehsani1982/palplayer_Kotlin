package com.hossainehs.palplayer.domain.sharedPreferences

import kotlinx.coroutines.flow.Flow

interface Preferences {

    suspend fun setCurrentFragmentPageNumber(pageNumber: Int)
    fun getCurrentFragmentPageNumber(): Flow<Int>

    suspend fun setPermissionStatus(status: Boolean)
    fun getPermissionStatus(): Flow<Boolean>

    suspend fun setIsAppCurrentlyPlaying(status: Boolean)
    fun getIsAppCurrentlyPlaying(): Flow<Boolean>

    suspend fun setCurrentlyPlayingFileName(fileName: String)
    fun getCurrentlyPlayingFileName(): Flow<String>

    suspend fun setCurrentlyPlayingFileSubCategory(fileSubCategory: String)
    fun getCurrentlyPlayingFileSubCategory(): Flow<String>

    suspend fun setCurrentlyPlayingFileCategory(fileCategory: String)
    fun getCurrentlyPlayingFileCategory(): Flow<String>

    suspend fun setNowPlayingAtt(nowPlayingAtt: Long)
    fun getNowPlayingAtt(): Flow<Long>

    suspend fun setMediaDuration(duration: Long)
    fun getMediaDuration():  Flow<Long>

    suspend fun setMediaItemUri(mediaItemUri: String)
    fun getMediaItemUri(): Flow<String>

    suspend fun setIsFileCurrentlyPlaying(status: Boolean)
    fun getIsFileCurrentlyPlaying(): Flow<Boolean>

}
