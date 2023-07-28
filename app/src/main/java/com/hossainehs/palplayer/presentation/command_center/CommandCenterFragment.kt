package com.hossainehs.palplayer.presentation.command_center

import android.animation.Animator
import android.app.Dialog
import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.res.Resources
import android.os.Bundle
import android.os.IBinder
import android.support.v4.media.MediaMetadataCompat
import android.view.View
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.hossainehs.palplayer.R
import com.hossainehs.palplayer.data.util.ConstValues
import com.hossainehs.palplayer.databinding.FragmentComandCenterBinding
import com.hossainehs.palplayer.domain.model.MediaFile
import com.hossainehs.palplayer.presentation.media_files.MediaFilesViewModel
import com.hossainehs.palplayer.presentation.media_files.MediaFilesViewModelEvents
import com.hossainehs.palplayer.presentation.util.CommandCenterEvents
import com.hossainehs.palplayer.service.MusicService
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CommandCenterFragment : BottomSheetDialogFragment(R.layout.fragment_comand_center) {

    companion object {
        var musicService: MusicService? = null
    }

    private val commandCenterViewModel: CommandCenterViewModel by viewModels()
    private lateinit var binding: FragmentComandCenterBinding
    lateinit var mediaFilesViewModel: MediaFilesViewModel
    private lateinit var dialog: BottomSheetDialog
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
    private var playingMediaFile: MediaFile? = null
    private var playStatus = false


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentComandCenterBinding.bind(view)

        mediaFilesViewModel = ViewModelProvider(
            requireActivity()
        )[MediaFilesViewModel::class.java]

        bottomSheetBehavior = BottomSheetBehavior.from((view.parent) as View)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        val metric = Resources.getSystem().displayMetrics
        val initialHeight = (metric.heightPixels * 0.25).toInt()
        bottomSheetBehavior.peekHeight = initialHeight

        //view functions
        subscribeToObservers()



        mediaFilesViewModel.currentlyPlayingMediaFile?.let {mediaFileLive->
            mediaFileLive.observe(viewLifecycleOwner){
                playingMediaFile = it
            }
        }


        binding.apply {
            mediaFilesViewModel.currentPlayingSong.observe(viewLifecycleOwner) {
                it?.let { mediaMetadata ->
                    mediaFilesViewModel.onEvent(
                        MediaFilesViewModelEvents.OnMediaItemCompatChanged(
                            mediaMetadata
                        )
                    )
                    tvMediaName.text = mediaMetadata.description.title
                    tvDuration.text = getString(
                        R.string.duration_text,
                        formatTime(mediaMetadata.getLong(MediaMetadataCompat.METADATA_KEY_DURATION)
                        )
                    )
                    progressBar.max = mediaMetadata.getLong(
                        MediaMetadataCompat.METADATA_KEY_DURATION
                    ).toInt()
                }
            }

            mediaFilesViewModel.isMediaFilePlaying.observe(
                viewLifecycleOwner
            ) { isPlaying ->
               playStatus = isPlaying
                btnPlayPauseAnimation(isPlaying)
            }

            mediaFilesViewModel.currentPosition.observe(viewLifecycleOwner) {
                progressBar.progress = it.toInt()
                tvCurrentProgress.text = formatTime(it)
            }


            progressBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
                var pb_position = 0
                override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                    pb_position = p1
                }

                override fun onStartTrackingTouch(p0: SeekBar?) {

                }

                override fun onStopTrackingTouch(p0: SeekBar?) {
                    if (playingMediaFile != null) {
                        mediaFilesViewModel.onEvent(
                            MediaFilesViewModelEvents.OnSeekTo(
                                pb_position.toLong()
                            )
                        )
                    }
                }

            })


            ltBtnPreviousSong.setOnClickListener {
                    mediaFilesViewModel.onEvent(
                        MediaFilesViewModelEvents.OnPreviousButtonClicked
                    )

            }

            ltBtn30SecRewind.setOnClickListener {
               mediaFilesViewModel.onEvent(
                   MediaFilesViewModelEvents.On30RewindButtonClicked
               )
            }

            ltBtnPlay.setOnClickListener {
                mediaFilesViewModel.onEvent(
                    MediaFilesViewModelEvents.OnPlayPauseButtonClicked(
                        playingMediaFile!!,
                        playStatus
                    )
                )

            }

            ltBtn10SecForward.setOnClickListener {
                mediaFilesViewModel.onEvent(
                    MediaFilesViewModelEvents.On10ForwardButtonClicked
                )
            }

            ltBtnNextSong.setOnClickListener {
                mediaFilesViewModel.onEvent(
                    MediaFilesViewModelEvents.OnNextButtonClicked
                )
            }

        }


        bottomSheetBehavior.addBottomSheetCallback(
            object :
                BottomSheetBehavior.BottomSheetCallback() {
                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    println("onSlide")
                }

                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    when (newState) {
                        BottomSheetBehavior.STATE_EXPANDED -> {
                            println("STATE_EXPANDED")
                        }

                        BottomSheetBehavior.STATE_COLLAPSED -> {
                            println("STATE_COLLAPSED")

                            bottomSheetBehavior.peekHeight = initialHeight
                            bottomSheetBehavior.isHideable = false
                        }

                        BottomSheetBehavior.STATE_DRAGGING -> {
                            println("STATE_DRAGGING")
                        }

                        else -> {

                        }
                    }
                }
            })
    }

    private fun subscribeToObservers() {
        commandCenterViewModel.commandCenterEvents.asLiveData().observe(
            viewLifecycleOwner
        ) { event ->
            when (event) {

                is CommandCenterEvents.OnProgressbarChanged -> {

                }

                CommandCenterEvents.OnPreviousClick -> {
                    binding.apply {
                        ltBtnPreviousSong.playAnimation()
                        ltBtnPreviousSong.repeatCount = 0
                    }

                }

                CommandCenterEvents.On30SecRewindClick -> {
                    binding.apply {
                        ltBtn30SecRewind.playAnimation()
                        ltBtn30SecRewind.repeatCount = 1
                    }

                }

                CommandCenterEvents.OnPlayClick -> {
                    mediaFilesViewModel.onEvent(
                        MediaFilesViewModelEvents.OnPlayPauseButtonClicked(
                            playingMediaFile!!,
                            !playStatus
                        )
                    )

                }

                CommandCenterEvents.OnPauseClick -> {
                }

                CommandCenterEvents.On10SecForwardClick -> {
                    binding.apply {
                        ltBtn10SecForward.playAnimation()
                        ltBtn10SecForward.repeatCount = 0
                    }

                }

                CommandCenterEvents.OnNextClick -> {
                    binding.apply {
                        ltBtnNextSong.playAnimation()
                        ltBtnNextSong.repeatCount = 0
                    }
                }


                is CommandCenterEvents.PlayPauseButtonAnimation -> {
                    playStatus = event.isPlaying
                    btnPlayPauseAnimation(playStatus)

                }


            }
        }
    }


    private fun btnPlayPauseAnimation(status: Boolean) {
        binding.apply {
            when (status) {
                true -> {
                    ltBtnPlay.setMaxFrame(175)
                    ltBtnPlay.removeAllUpdateListeners()
                    ltBtnPlay.addAnimatorListener(object : Animator.AnimatorListener {
                        override fun onAnimationStart(animator: Animator) {}
                        override fun onAnimationEnd(animator: Animator) {}
                        override fun onAnimationCancel(animator: Animator) {}
                        override fun onAnimationRepeat(animator: Animator) {
                            ltBtnPlay.setMinFrame(90)
                        }
                    })
                    ltBtnPlay.resumeAnimation()
                }

                false -> {
                    ltBtnPlay.removeAllAnimatorListeners()
                    ltBtnPlay.addAnimatorUpdateListener { valueAnimator ->
                        if (valueAnimator.animatedFraction == 1f) {
                            ltBtnPlay.setMinFrame(60)
                            ltBtnPlay.pauseAnimation()
                        }
                    }
                    ltBtnPlay.setMinFrame(175)
                    ltBtnPlay.setMaxFrame(210)


                }
            }
        }
    }



    private fun formatTime(milliseconds: Long): String {
        val seconds = (milliseconds / 1000).toInt()
        val hours = seconds / 3600
        val minutes = (seconds % 3600) / 60
        val remainingSeconds = seconds % 60

        return String.format("%02d:%02d:%02d", hours, minutes, remainingSeconds)
    }


}