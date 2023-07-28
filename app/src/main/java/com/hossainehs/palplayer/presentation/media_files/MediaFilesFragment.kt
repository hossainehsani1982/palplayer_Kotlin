package com.hossainehs.palplayer.presentation.media_files


import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.hossainehs.mediaplayer.data.util.Status
import com.hossainehs.palplayer.R
import com.hossainehs.palplayer.databinding.FragmentAudioFilesBinding
import com.hossainehs.palplayer.domain.model.MediaFile
import com.hossainehs.palplayer.presentation.command_center.CommandCenterFragment
import com.hossainehs.palplayer.presentation.command_center.CommandCenterViewModelEvents
import com.hossainehs.palplayer.presentation.util.MediaFilesEvents
import com.hossainehs.palplayer.service.MusicService
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MediaFilesFragment :
    Fragment(R.layout.fragment_audio_files),
    MediaFilesAdapter.MediaFilesAdapterListener {

    private lateinit var binding: FragmentAudioFilesBinding
    lateinit var mediaFilesViewModel: MediaFilesViewModel
    private lateinit var mediaFilesAdapter: MediaFilesAdapter
    private var playStatus = false


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentAudioFilesBinding.bind(view)
        mediaFilesViewModel = ViewModelProvider(
            requireActivity()
        )[MediaFilesViewModel::class.java]

        subscribeToObservers()


        mediaFilesViewModel.isMediaFilePlaying.observe(
            viewLifecycleOwner
        ) { isPlaying ->
            playStatus = isPlaying
        }


        mediaFilesAdapter = MediaFilesAdapter(this)
        mediaFilesViewModel.mediaItems.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {

                    it.data?.let { mediaFiles ->
                        mediaFilesAdapter.submitList(mediaFiles)
                    }
                }

                Status.LOADING -> {

                }

                Status.ERROR -> {

                }
            }
            mediaFilesAdapter.submitList(it.data)
        }


        binding.apply {
            ivBack.setOnClickListener {
                mediaFilesViewModel.onEvent(
                    MediaFilesViewModelEvents.NavigateBack
                )
            }

            ltExploreFiles.setOnClickListener {
                mediaFilesViewModel.onEvent(
                    MediaFilesViewModelEvents.NavigateToSysTemMediaFiles
                )
            }
            rvMediaFiles.apply {
                adapter = mediaFilesAdapter
                layoutManager = LinearLayoutManager(requireContext())
                addItemDecoration(
                    DividerItemDecoration(
                        requireContext(),
                        DividerItemDecoration.VERTICAL
                    )
                )
            }


        }


    }

    private fun subscribeToObservers() {
        mediaFilesViewModel.mediaFilesEvents.asLiveData().observe(viewLifecycleOwner) { event ->
            when (event) {
                is MediaFilesEvents.NavigateBack -> {
                    setFragmentResult(
                        "subCategoryID", bundleOf(
                            "subCategoryID" to
                                    event.mainCategoryNumber
                        )
                    )
                    findNavController().popBackStack()
                }
                is MediaFilesEvents.NavigateToSysTemMediaFiles -> {
                    val action = MediaFilesFragmentDirections
                        .actionAudioFilesFragmentToSystemMediaFilesFragment(
                            subCategoryId = event.subCategoryId
                        )
                    findNavController().navigate(action)
                }
                MediaFilesEvents.PauseMusic -> TODO()
                is MediaFilesEvents.PlayMusic -> TODO()
            }


        }
    }

    override fun onBtnPlayPauseClicked(mediaFile: MediaFile) {
        mediaFilesViewModel.onEvent(
            MediaFilesViewModelEvents.OnPlayPauseButtonClicked(
                mediaFile,
                playStatus
            )
        )
    }

//    private fun bindService() {
//        val bindingServiceIntent = Intent(requireContext(), MusicService::class.java)
//
//        val serviceConnection = object : ServiceConnection {
//            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
//                // Service connected, you can interact with the service here
//                val binder = service as MusicService.PalPlayerBinder
//                CommandCenterFragment.musicService = binder.getService()
//                // Use myService instance as needed
//                //requireContext().startForegroundService(bindingServiceIntent)
//            }
//
//            override fun onServiceDisconnected(name: ComponentName?) {
//                // Service disconnected, clean up any references
//            }
//        }
//        requireContext().bindService(
//            bindingServiceIntent,
//            serviceConnection,
//            Context.BIND_AUTO_CREATE
//        )
//    }



    //package:mine




}