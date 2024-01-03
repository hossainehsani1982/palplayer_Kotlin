package com.hossainehs.palplayer.player_service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import androidx.media3.ui.PlayerNotificationManager
import com.hossainehs.palplayer.R
import com.hossainehs.palplayer.data.util.ConstValues.NOTIFICATION_CHANNEL_ID
import com.hossainehs.palplayer.data.util.ConstValues.NOTIFICATION_CHANNEL_NAME
import com.hossainehs.palplayer.data.util.ConstValues.NOTIFICATION_ID
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AppNotificationManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val exoPlayer: ExoPlayer,
) {
    private val notificationManager: NotificationManagerCompat =
        NotificationManagerCompat.from(context)

    init {
        createNotificationChannel()
    }

    @UnstableApi
    fun startNotificationService(
        mediaSessionService: MediaSessionService,
        mediaSession: MediaSession,
    ) {
        buildNotification(mediaSession)
        startForeGroundNotificationService(mediaSessionService)
    }

    private fun startForeGroundNotificationService(mediaSessionService: MediaSessionService) {
        val notification = Notification.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setCategory(Notification.CATEGORY_SERVICE)
            .build()
        mediaSessionService.startForeground(NOTIFICATION_ID, notification)
    }

    @UnstableApi
    private fun buildNotification(mediaSession: MediaSession) {
        PlayerNotificationManager.Builder(
            context,
            NOTIFICATION_ID,
            NOTIFICATION_CHANNEL_ID
        )
            .setMediaDescriptionAdapter(
                AppNotificationAdapter(
                    context = context,
                    pendingIntent = mediaSession.sessionActivity
                )
            )
            .setSmallIconResourceId(R.drawable.icon_logo)
            .build()
            .also {
                it.setMediaSessionToken(mediaSession.sessionCompatToken)
                it.setUseFastForwardActionInCompactView(true)
                it.setUseRewindActionInCompactView(true)
                it.setUseNextActionInCompactView(true)
                it.setPriority(NotificationCompat.PRIORITY_LOW)
                it.setPlayer(exoPlayer)
            }
    }


    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_LOW
        )
        notificationManager.createNotificationChannel(channel)
    }
}