package com.hossainehs.palplayer.data.sharedPreferences

import com.hossainehs.palplayer.domain.sharedPreferences.Preferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PreferencesImpl (
    private val preferencesDataStore: PreferencesDataStore
        ) : Preferences {
    override suspend fun setCurrentFragmentPageNumber(pageNumber: Int) {
        preferencesDataStore.updateCurrentFragmentPageNumber(pageNumber)
    }

    override fun getCurrentFragmentPageNumber(): Flow<Int> {
        return preferencesDataStore.preferencesFlow.map {
            it.currentFragmentPageNumber }
    }

    override suspend fun setPermissionStatus(status: Boolean) {
        return preferencesDataStore.updateIsPermissionsGranted(status)
    }

    override fun getPermissionStatus(): Flow<Boolean> {
        return preferencesDataStore.preferencesFlow.map {
            it.isPermissionsGranted }
    }

    override suspend fun setIsAppCurrentlyPlaying(status: Boolean) {
        preferencesDataStore.updateIsAppCurrentlyPlaying(status)
    }

    override fun getIsAppCurrentlyPlaying(): Flow<Boolean> {
        return preferencesDataStore.preferencesFlow.map {
            it.isAppCurrentlyPlaying }
    }

    override suspend fun setCurrentlyPlayingFileName(fileName: String) {
        preferencesDataStore.updateCurrentlyPlayingFileName(fileName)
    }

    override fun getCurrentlyPlayingFileName(): Flow<String> {
        return preferencesDataStore.preferencesFlow.map {
            it.currentlyPlayingFileName }
    }

    override suspend fun setCurrentlyPlayingFileSubCategory(fileSubCategory: String) {
        preferencesDataStore.updateCurrentlyPlayingFileSubCategory(fileSubCategory)
    }

    override fun getCurrentlyPlayingFileSubCategory(): Flow<String> {
        return preferencesDataStore.preferencesFlow.map {
            it.currentlyPlayingFileSubCategory }
    }

    override suspend fun setCurrentlyPlayingFileCategory(fileCategory: String) {
        preferencesDataStore.updateCurrentlyPlayingFileCategory(fileCategory)
    }

    override fun getCurrentlyPlayingFileCategory(): Flow<String> {
        return preferencesDataStore.preferencesFlow.map {
            it.currentlyPlayingFile }
    }

    override suspend fun setNowPlayingAtt(nowPlayingAtt: Long) {
        preferencesDataStore.updateNowPlayingAtt(nowPlayingAtt)
    }

    override fun getNowPlayingAtt(): Flow<Long> {
        return preferencesDataStore.preferencesFlow.map {
            it.nowPlayingAtt }
    }

    override suspend fun setMediaDuration(duration: Long) {
        preferencesDataStore.updateDuration(duration)
    }

    override fun getMediaDuration(): Flow<Long> {
        return preferencesDataStore.preferencesFlow.map {
            it.duration }
    }

    override suspend fun setMediaItemUri(mediaItemUri: String) {
        preferencesDataStore.updateContentUri(mediaItemUri)
    }

    override fun getMediaItemUri(): Flow<String> {
        return preferencesDataStore.preferencesFlow.map {
            it.contentUri }
    }

    override suspend fun setIsFileCurrentlyPlaying(status: Boolean) {
        preferencesDataStore.updateIsFileCurrentlyPlaying(status)
    }

    override fun getIsFileCurrentlyPlaying(): Flow<Boolean> {
        return preferencesDataStore.preferencesFlow.map {
            it.isFileCurrentlyPlaying }
    }


}