package com.hossainehs.palplayer.presentation.command_center

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hossainehs.palplayer.domain.sharedPreferences.Preferences
import com.hossainehs.palplayer.domain.use_case.UseCases
import com.hossainehs.palplayer.presentation.util.CommandCenterEvents
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CommandCenterViewModel @Inject constructor(
    private val preferences: Preferences,
    private val useCases: UseCases
) : ViewModel() {

    val state = CommandCenterState()

    private val _commandCenterEvents = Channel<CommandCenterEvents>()
    val commandCenterEvents = _commandCenterEvents.receiveAsFlow()


    init {
        checkIfAppIsCurrentlyPlaying()
    }

    fun onEvent(event: CommandCenterViewModelEvents) {
        when (event) {

            is CommandCenterViewModelEvents.OnProgressbarChanged -> {
                viewModelScope.launch {
                    _commandCenterEvents.send(
                        CommandCenterEvents.OnProgressbarChanged(
                            event.position
                        )
                    )
                }
            }

            is CommandCenterViewModelEvents.OnPreviousButtonClicked -> {
                viewModelScope.launch {
                    _commandCenterEvents.send(CommandCenterEvents.OnPreviousClick)
                }
            }

            CommandCenterViewModelEvents.On30RewindButtonClicked -> {
                viewModelScope.launch {
                    _commandCenterEvents.send(CommandCenterEvents.On30SecRewindClick)
                }
            }


            is CommandCenterViewModelEvents.OnPlayPauseButtonClicked -> {
                viewModelScope.launch {
                    when (event.isPlaying) {
                        true -> {
                            _commandCenterEvents.send(
                                CommandCenterEvents.OnPlayClick
                            )
                        }

                        false -> {
                            _commandCenterEvents.send(
                                CommandCenterEvents.OnPauseClick
                            )
                        }
                    }
                }
            }

            CommandCenterViewModelEvents.On10ForwardButtonClicked -> {
                viewModelScope.launch {
                    _commandCenterEvents.send(CommandCenterEvents.On10SecForwardClick)
                }
            }

            is CommandCenterViewModelEvents.OnNextButtonClicked -> {
                viewModelScope.launch {
                    _commandCenterEvents.send(CommandCenterEvents.OnNextClick)
                }
            }

        }
    }
}

private fun checkIfAppIsCurrentlyPlaying() {

}




