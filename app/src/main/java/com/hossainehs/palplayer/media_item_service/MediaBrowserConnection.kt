package com.hossainehs.palplayer.media_item_service

import android.content.ComponentName
import android.content.Context
import android.support.v4.media.MediaBrowserCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hossainehs.mediaplayer.data.util.Event
import com.hossainehs.mediaplayer.data.util.Resource

class MediaBrowserConnection (
    context : Context,
) {
    private val _isConnected = MutableLiveData<Event<Resource<Boolean>>>()
    val isConnected: LiveData<Event<Resource<Boolean>>> = _isConnected

    private val _databaseError = MutableLiveData<Event<Resource<Boolean>>>()
    val databaseError: LiveData<Event<Resource<Boolean>>> = _databaseError

    private val mediaBrowserConnectionCallback = MediaBrowserConnectionCallback()

    private val mediaBrowser = MediaBrowserCompat(
        context,
        ComponentName(
            context,
            MediaBrowserService::class.java
        ),
        mediaBrowserConnectionCallback,
        null
    ).apply { connect() }

    private inner class MediaBrowserConnectionCallback() : MediaBrowserCompat.ConnectionCallback() {
        override fun onConnected() {
            println("MediaBrowserConnection, onConnected")
            _isConnected.postValue(Event(Resource.success(true)))
        }

        override fun onConnectionSuspended() {
            _isConnected.postValue(Event(Resource.error(
                "The connection was suspended",
                false)))
        }

        override fun onConnectionFailed() {
            _isConnected.postValue(Event(Resource.error(
                "Couldn't connect to media browser",
                false)))
        }
    }

    fun subscribe(
        parentId: String,
        callback: MediaBrowserCompat.SubscriptionCallback) {
        mediaBrowser.subscribe(parentId, callback)
    }

    fun unsubscribe(parentId: String,
                    callback: MediaBrowserCompat.SubscriptionCallback) {
        mediaBrowser.unsubscribe(parentId, callback)
    }

}