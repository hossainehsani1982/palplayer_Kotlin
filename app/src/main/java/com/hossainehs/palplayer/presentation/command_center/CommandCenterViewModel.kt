package com.hossainehs.palplayer.presentation.command_center

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hossainehs.palplayer.domain.sharedPreferences.Preferences
import com.hossainehs.palplayer.player_service.AudioServicePlaybackHandler
import com.hossainehs.palplayer.presentation.media_files.UIState
import com.hossainehs.palplayer.presentation.util.CommandCenterEvents
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CommandCenterViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val preferences: Preferences
) : ViewModel() {

    val state = CommandCenterState(
        savedStateHandle = savedStateHandle
    )

    init {
        subscribeToPreferences()
    }

    private val _commandCenterEvents = Channel<CommandCenterEvents>()
    val commandCenterEvents = _commandCenterEvents.receiveAsFlow()

    private val _uiState: MutableStateFlow<UIState> = MutableStateFlow(UIState.Initial)
    val uiState: StateFlow<UIState> = _uiState.asStateFlow()

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
                                CommandCenterEvents.OnPlayPauseClick
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

    private fun subscribeToPreferences(){
        viewModelScope.launch {
            preferences.getCurrentlyPlayingFileName().collect {
                println("CommandCenterViewModel: $it")
                state.updateCurrentlyPlayingName(it)
            }
        }
    }

}






