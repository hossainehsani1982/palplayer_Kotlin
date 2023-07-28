package com.hossainehs.palplayer.service.callbacks

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.support.v4.media.session.MediaSessionCompat
import androidx.core.content.ContextCompat
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.hossainehs.palplayer.service.MusicService
import com.hossainehs.palplayer.data.util.ConstValues.NOTIFICATION_ID


//media playback preparer

class MusicPlayerNotificationListener(
    private val musicService: MusicService,
    ) : PlayerNotificationManager.NotificationListener{

    override fun onNotificationCancelled(notificationId: Int, dismissedByUser: Boolean) {
        super.onNotificationCancelled(notificationId, dismissedByUser)

        musicService.apply {
            stopForeground(Service.STOP_FOREGROUND_DETACH)
            isForegroundService = false
            stopSelf()
        }
    }

    override fun onNotificationPosted(
        notificationId: Int,
        notification: Notification,
        ongoing: Boolean
    ) {
        super.onNotificationPosted(notificationId, notification, ongoing)
        musicService.apply {
            if(ongoing && !isForegroundService){
                ContextCompat.startForegroundService(
                    this,
                    Intent(applicationContext, this::class.java)
                )
                startForeground(NOTIFICATION_ID, notification)
                isForegroundService = true
            }
        }
    }
}