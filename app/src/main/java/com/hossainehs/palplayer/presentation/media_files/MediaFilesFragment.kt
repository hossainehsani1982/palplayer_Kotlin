package com.hossainehs.palplayer.presentation.media_files


import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat.startForegroundService
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.hossainehs.palplayer.R
import com.hossainehs.palplayer.databinding.FragmentAudioFilesBinding
import com.hossainehs.palplayer.domain.model.MediaFile
import com.hossainehs.palplayer.player_service.PlayerService
import com.hossainehs.palplayer.presentation.util.MediaFilesEvents
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MediaFilesFragment :
    Fragment(R.layout.fragment_audio_files),
    MediaFilesAdapter.MediaFilesAdapterListener {

    private lateinit var binding: FragmentAudioFilesBinding
    private val mediaFilesViewModel: MediaFilesViewModel by viewModels()
    private lateinit var mediaFilesAdapter: MediaFilesAdapter
    private var isServiceRunning = false


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentAudioFilesBinding.bind(view)
//        mediaFilesViewModel = ViewModelProvider(
//            requireActivity()
//        )[MediaFilesViewModel::class.java]

        subscribeToObservers()
        mediaFilesAdapter = MediaFilesAdapter(this)



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
            mediaFilesViewModel.state.mediaFilesList.observe(viewLifecycleOwner) { mediaFiles ->
                mediaFiles?.let {
                    mediaFilesAdapter.submitList(it)
                }
            }
        }
        /**
        will fix the issue of not updating the MediaFile list. when  new media file/s are added,
        it will call the onEvent function in the viewModel to load the media files again
         **/
        setFragmentResultListener("isMediaAdded") { _, bundle ->
            val isMediaAdded = bundle.getBoolean("isMediaAdded")
            if (isMediaAdded) {
                mediaFilesViewModel.onEvent(
                    MediaFilesViewModelEvents.OnMediaAdded
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
                    val action =
                        MediaFilesFragmentDirections.actionMediaFilesFragmentToSystemMediaFilesFragment(
                            event.subCategoryId
                        )
                    findNavController().navigate(
                        action
                    )
                }

                is MediaFilesEvents.PlayPauseMusic -> {
                    mediaFilesViewModel.onEvent(
                        MediaFilesViewModelEvents.OnPlayPauseButtonClicked
                    )
                    startService()
                }

            }
        }
    }

    private fun startService() {
        if (!isServiceRunning) {
            val intent = Intent(requireContext(), PlayerService::class.java)
            startForegroundService(requireContext(), intent)
            isServiceRunning = true
        }
    }

    override fun onBtnPlayPauseClicked(mediaFile: MediaFile) {
        startService()

        mediaFilesViewModel.onEvent(
            MediaFilesViewModelEvents.OnSelectedAudioChange(
                mediaFile
            )
        )
    }


}