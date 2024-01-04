package com.hossainehs.palplayer.presentation.command_center

import android.animation.Animator
import android.app.Dialog
import android.content.res.Resources
import android.os.Bundle
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
import com.hossainehs.palplayer.databinding.FragmentComandCenterBinding
import com.hossainehs.palplayer.domain.model.MediaFile
import com.hossainehs.palplayer.presentation.media_files.MediaFilesViewModel
import com.hossainehs.palplayer.presentation.media_files.MediaFilesViewModelEvents
import com.hossainehs.palplayer.presentation.util.CommandCenterEvents
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CommandCenterFragment : BottomSheetDialogFragment(R.layout.fragment_comand_center) {


    private val commandCenterViewModel: CommandCenterViewModel by viewModels()
    private val mediaFilesViewModel: MediaFilesViewModel by viewModels()
    //private lateinit var mediaFilesViewModel: MediaFilesViewModel
    private lateinit var binding: FragmentComandCenterBinding
    private lateinit var dialog: BottomSheetDialog
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
    private var playingMediaFile: MediaFile? = null
    private var isPlaying: Boolean = false


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentComandCenterBinding.bind(view)

//        mediaFilesViewModel = ViewModelProvider(
//            requireActivity()
//        )[MediaFilesViewModel::class.java]

        bottomSheetBehavior = BottomSheetBehavior.from((view.parent) as View)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        val metric = Resources.getSystem().displayMetrics
        val initialHeight = (metric.heightPixels * 0.25).toInt()
        bottomSheetBehavior.peekHeight = initialHeight

        //view functions
        subscribeToObservers()



        binding.apply {

            mediaFilesViewModel.state.playingFileName.observe(viewLifecycleOwner) {
                println("displayName1: ${it}")
                tvMediaName.text = it
            }

            mediaFilesViewModel.state.durationString.observe(viewLifecycleOwner) {
                tvDuration.text = it
            }
            mediaFilesViewModel.state.progressString.observe(viewLifecycleOwner) {

                tvCurrentProgress.text = it
            }

            mediaFilesViewModel.state.currentPosition.observe(viewLifecycleOwner) {

            }



            mediaFilesViewModel.state.isPlaying.observe(viewLifecycleOwner) {
                if (it) {
                    btnPlayPauseAnimation(true)
                } else {
                    btnPlayPauseAnimation(false)
                }
                isPlaying = it
            }


            mediaFilesViewModel.state.progress.observe(viewLifecycleOwner) {
                progressBar.progress = it.toInt()
            }
            progressBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
                var pb_position = 0
                override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                    pb_position = p1
                }

                override fun onStartTrackingTouch(p0: SeekBar?) {

                }

                override fun onStopTrackingTouch(p0: SeekBar?) {
                    // if (playingMediaFile != null) {
                    mediaFilesViewModel.onEvent(
                        MediaFilesViewModelEvents.OnSeekTo(
                            pb_position.toLong()
                        )
                    )
                    // }
                }

            })

            ltBtnPreviousSong.setOnClickListener {
                mediaFilesViewModel.onEvent(
                    MediaFilesViewModelEvents.OnPreviousButtonClicked
                )

            }

            ltBtn30SecRewind.setOnClickListener {
//               mediaFilesViewModel.onEvent(
//                   MediaFilesViewModelEvents.On30RewindButtonClicked
//               )
            }

            ltBtnPlay.setOnClickListener {

                commandCenterViewModel.onEvent(
                    CommandCenterViewModelEvents.OnPlayPauseButtonClicked(
                        currentPosition = mediaFilesViewModel.state.currentPosition.value!!,
                        isPlaying = isPlaying,
                        mediaFile = playingMediaFile!!
                    )
                )

            }

            ltBtn10SecForward.setOnClickListener {
//                mediaFilesViewModel.onEvent(
//                    MediaFilesViewModelEvents.On10ForwardButtonClicked
//                )
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

                CommandCenterEvents.OnPlayPauseClick -> {
                    mediaFilesViewModel.onEvent(
                        MediaFilesViewModelEvents.OnPlayPauseButtonClicked
                    )
                    btnPlayPauseAnimation(!isPlaying)
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