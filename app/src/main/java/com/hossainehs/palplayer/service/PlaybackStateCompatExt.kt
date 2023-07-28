package com.hossainehs.palplayer.service

import android.support.v4.media.session.PlaybackStateCompat

inline val PlaybackStateCompat.isPrepared
    get() = state == PlaybackStateCompat.STATE_BUFFERING ||
            state == PlaybackStateCompat.STATE_PLAYING ||
            state == PlaybackStateCompat.STATE_PAUSED

inline val PlaybackStateCompat.isPlaying
    get() = state == PlaybackStateCompat.STATE_BUFFERING ||
            state == PlaybackStateCompat.STATE_PLAYING


//check if play option is enabled
inline val PlaybackStateCompat.isPlayEnabled
    get() =
        //check if play option is enabled
        actions and PlaybackStateCompat.ACTION_PLAY != 0L ||
            //check id play/pause option is enabled
            (actions and PlaybackStateCompat.ACTION_PLAY_PAUSE != 0L &&
                    state == PlaybackStateCompat.STATE_PAUSED)