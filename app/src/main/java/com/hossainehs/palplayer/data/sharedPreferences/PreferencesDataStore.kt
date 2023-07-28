package com.hossainehs.palplayer.data.sharedPreferences

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.map


private val Context.datastore by preferencesDataStore("EzCare_DataStore")

data class DataPreferences(val currentFragmentPageNumber : Int,
                           val isAppCurrentlyPlaying: Boolean,
                           val isPermissionsGranted : Boolean,
                           val currentlyPlayingFileName : String,
                           val currentlyPlayingFileSubCategory : String,
                           val currentlyPlayingFile : String,
                           val isFileCurrentlyPlaying : Boolean,
                           val nowPlayingAtt : Long,
                           val contentUri : String,
                           val duration: Long
            )

class PreferencesDataStore (@ApplicationContext context: Context){
    private val datastore = context.datastore

    val preferencesFlow = datastore.data.map {
        val currentFragmentPageNumber = it[PreferencesKeys.CURRENT_FRAGMENT_PAGE_NUMBER] ?: 0
        val isAppCurrentlyPlaying = it[PreferencesKeys.IS_APP_CURRENTLY_PLAYING] ?: false
        val isPermissionsGranted = it[PreferencesKeys.IS_PERMISSIONS_GRANTED] ?: false
        val currentlyPlayingFileName = it[PreferencesKeys.CURRENTLY_PLAYING_FILE_NAME] ?: ""
        val currentlyPlayingFileSubCategory = it[PreferencesKeys.CURRENTLY_PLAYING_FILE_SUB_CATEGORY] ?: ""
        val currentlyPlayingFileCategory = it[PreferencesKeys.CURRENTLY_PLAYING_FILE_CATEGORY] ?: ""
        val nowPlayingAtt = it[PreferencesKeys.NOW_PLAYING_ATT] ?: 0L
        val contentUri = it[PreferencesKeys.CONTENT_URI] ?: ""
        val duration = it[PreferencesKeys.DURATION] ?: 0L
        val isFileCurrentlyPlaying = it[PreferencesKeys.IS_FILE_CURRENTLY_PLAYING] ?: false
        DataPreferences(currentFragmentPageNumber,
            isAppCurrentlyPlaying,
            isPermissionsGranted,
            currentlyPlayingFileName,
            currentlyPlayingFileSubCategory,
            currentlyPlayingFileCategory,
            isFileCurrentlyPlaying,
            nowPlayingAtt,
            contentUri,
            duration
        )
    }

    suspend fun updateCurrentFragmentPageNumber(currentFragmentPageNumber : Int){
        datastore.edit { preference ->
            preference[PreferencesKeys.CURRENT_FRAGMENT_PAGE_NUMBER] = currentFragmentPageNumber
        }
    }
    suspend fun updateIsPermissionsGranted(isPermissionsGranted : Boolean){
        datastore.edit { preferences ->
            preferences[PreferencesKeys.IS_PERMISSIONS_GRANTED] = isPermissionsGranted
        }
    }
    suspend fun updateIsAppCurrentlyPlaying(isAppCurrentlyPlaying : Boolean){
        datastore.edit { preferences ->
            preferences[PreferencesKeys.IS_APP_CURRENTLY_PLAYING] = isAppCurrentlyPlaying
        }
    }
    suspend fun updateCurrentlyPlayingFileName(currentlyPlayingFileName : String){
        datastore.edit { preferences ->
            preferences[PreferencesKeys.CURRENTLY_PLAYING_FILE_NAME] = currentlyPlayingFileName
        }
    }
    suspend fun updateCurrentlyPlayingFileSubCategory(currentlyPlayingFileSubCategory : String){
        datastore.edit { preferences ->
            preferences[PreferencesKeys.CURRENTLY_PLAYING_FILE_SUB_CATEGORY] = currentlyPlayingFileSubCategory
        }
    }
    suspend fun updateCurrentlyPlayingFileCategory(currentlyPlayingFileCategory : String){
        datastore.edit { preferences ->
            preferences[PreferencesKeys.CURRENTLY_PLAYING_FILE_CATEGORY] = currentlyPlayingFileCategory
        }
    }
    suspend fun updateNowPlayingAtt(nowPlayingAtt : Long){
        datastore.edit { preferences ->
            preferences[PreferencesKeys.NOW_PLAYING_ATT] = nowPlayingAtt
        }
    }
    suspend fun updateContentUri(contentUri : String){
        datastore.edit { preferences ->
            preferences[PreferencesKeys.CONTENT_URI] = contentUri
        }
    }
    suspend fun updateDuration(duration : Long){
        datastore.edit { preferences ->
            preferences[PreferencesKeys.DURATION] = duration
        }
    }
    suspend fun updateIsFileCurrentlyPlaying(isFileCurrentlyPlaying : Boolean){
        datastore.edit { preferences ->
            preferences[PreferencesKeys.IS_FILE_CURRENTLY_PLAYING] = isFileCurrentlyPlaying
        }
    }

    private object PreferencesKeys{
        val CURRENT_FRAGMENT_PAGE_NUMBER = intPreferencesKey(
            "currentFragmentPageNumber"
        )
        val IS_PERMISSIONS_GRANTED = booleanPreferencesKey(
            "isPermissionsGranted"
        )
        val IS_APP_CURRENTLY_PLAYING = booleanPreferencesKey(
            "isAppCurrentlyPlaying"
        )
        val CURRENTLY_PLAYING_FILE_NAME = stringPreferencesKey(
            "currentlyPlayingFileName"
        )
        val CURRENTLY_PLAYING_FILE_SUB_CATEGORY = stringPreferencesKey(
            "currentlyPlayingFileSubCategory"
        )
        val CURRENTLY_PLAYING_FILE_CATEGORY = stringPreferencesKey(
            "currentlyPlayingFileCategory"
        )
        val NOW_PLAYING_ATT = longPreferencesKey(
            "nowPlayingAtt"
        )
        val CONTENT_URI = stringPreferencesKey(
            "contentUri"
        )
        val DURATION = longPreferencesKey(
            "duration"
        )
        val IS_FILE_CURRENTLY_PLAYING = booleanPreferencesKey(
            "isFileCurrentlyPlaying"
        )
    }
}

