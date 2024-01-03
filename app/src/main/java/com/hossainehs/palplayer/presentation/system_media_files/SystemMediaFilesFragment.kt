package com.hossainehs.palplayer.presentation.system_media_files

import android.content.ContentResolver
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.hossainehs.palplayer.R
import com.hossainehs.palplayer.databinding.FragmentSystemMediaFilesBinding
import com.hossainehs.palplayer.domain.model.SystemMediaFile
import com.hossainehs.palplayer.presentation.util.SystemMediaEvents
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
@RequiresApi(Build.VERSION_CODES.Q)
class SystemMediaFilesFragment :
    Fragment(R.layout.fragment_system_media_files),
    SystemMediaFileAdapter.OnItemClickListener {

    private lateinit var binding: FragmentSystemMediaFilesBinding
    private val systemMediaFileViewModel: SystemMediaViewModel by viewModels()
    private lateinit var systemMediaFileAdapter: SystemMediaFileAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentSystemMediaFilesBinding.bind(view)

        systemMediaFileAdapter = SystemMediaFileAdapter(this)

        subscribeToObservers()

        binding.apply {
            rvSystemMediaFiles.apply {
                adapter = systemMediaFileAdapter
                addItemDecoration(
                    DividerItemDecoration(
                        view.context,
                        DividerItemDecoration.VERTICAL
                    )
                )
                layoutManager = LinearLayoutManager(requireContext())
                systemMediaFileViewModel.state.systemMediaFileList.observe(
                    viewLifecycleOwner
                ) { systemMediaFileList ->
                    systemMediaFileAdapter.submitList(systemMediaFileList)
                }
            }

            btnDone.setOnClickListener {
                systemMediaFileViewModel.onEvents(
                    SystemMediaViewModelEvents.OnBtnDoneClicked
                )
            }
        }
    }

    private fun subscribeToObservers() {
        systemMediaFileViewModel.systemMediaEvents.asLiveData()
            .observe(viewLifecycleOwner) { event ->
                when (event) {
                    is SystemMediaEvents.OnBtnDoneClicked -> {
                        setFragmentResult(
                            "isMediaAdded", bundleOf(
                                "isMediaAdded" to
                                        event.isMediaAdded
                            )
                        )
                        findNavController().popBackStack()
                    }
                }
            }
    }

    override fun onItemSelected(systemMediaFile: SystemMediaFile) {
        systemMediaFileViewModel.onEvents(
            SystemMediaViewModelEvents.AddSelectedMediaFile(
                systemMediaFile
            )
        )
    }

    override fun onItemUnSelected(systemMediaFile: SystemMediaFile) {
        systemMediaFileViewModel.onEvents(
            SystemMediaViewModelEvents.RemoveSelectedMediaFile(
                systemMediaFile
            )
        )
    }
}