package com.hossainehs.palplayer.presentation.command_center

import com.hossainehs.palplayer.service.MusicService
import kotlinx.coroutines.flow.Flow

class CommandCenterState () {

    var progressBarPosition : Flow<Long>? = null

    var duration : Flow<Long>? = null




}