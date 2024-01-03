package com.hossainehs.palplayer.presentation.sub_category

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.hossainehs.palplayer.R
import com.hossainehs.palplayer.data.util.ConstValues.NOTIFICATION_ID
import com.hossainehs.palplayer.databinding.FragmentMainCategoryBinding
import com.hossainehs.palplayer.domain.model.relation.SubCategoryWithMediaFile
import com.hossainehs.palplayer.media_item_service.MediaBrowserService
import com.hossainehs.palplayer.presentation.util.SubCategoryPageEvents
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SubCategoryFragment : Fragment(R.layout.fragment_main_category),
    SubCategoryAdapter.OnItemClickListener {
    private lateinit var binding: FragmentMainCategoryBinding
    private val subCategoryViewModel: SubCategoryViewModel by viewModels()
    private lateinit var subCategoryAdapter: SubCategoryAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMainCategoryBinding.bind(view)

        subCategoryAdapter = SubCategoryAdapter(this)

        subscribeToObserves()
       // startService()

        /*
        * update the subCategoryViewModel with the current fragment page number
        * using the flow from the shared preferences
        */
        viewLifecycleOwner.lifecycleScope.launch {
            subCategoryViewModel.pref.getCurrentFragmentPageNumber().collect {
                subCategoryViewModel.onEvents(
                    SubCategoryViewModelEvents.OnSubCategoryChanged(
                        it
                    )
                )
            }
        }

        binding.apply {
            /*
            * setting up the recycler view for the sub categories
            */
            rvSubCategories.apply {
                adapter = subCategoryAdapter
                addItemDecoration(
                    DividerItemDecoration(
                        view.context,
                        DividerItemDecoration.VERTICAL
                    )
                )
                layoutManager = LinearLayoutManager(requireContext())

            }

            /*
            * setting up the add new folder button to pop up an alert dialog
             */

            btnAddNewFolder.setOnClickListener {
                val dialogView = LayoutInflater.from(requireContext())
                    .inflate(
                        R.layout.dialog_rounded_corners,
                        null
                    )
                val editText = dialogView.findViewById<EditText>(
                    R.id.et_new_folder_name
                )

                val builder = AlertDialog.Builder(requireContext())
                builder.setView(dialogView)
                builder.setTitle("Add New Folder")

                // Set the positive button
                builder.setPositiveButton("OK") { _, _ ->
                    subCategoryViewModel.onEvents(
                        SubCategoryViewModelEvents.OnAddNewSubCategory(
                            editText.text.toString()
                        )
                    )
                }

                // Set the negative button
                builder.setNegativeButton("Cancel") { dialog, _ ->
                    // Handle the negative button click event
                    dialog.cancel()
                }

                val dialog = builder.create()
                dialog.show()

            }
        }

        setFragmentResultListener("subCategoryID") { _, bundle ->
            val result = bundle.getInt("subCategoryID")
            subCategoryViewModel.onEvents(
                SubCategoryViewModelEvents.OnReturnToSubCategory(
                    result
                )
            )
        }
    }

    private fun subscribeToObserves() {
        subCategoryViewModel.mainCategoryEvents.asLiveData().observe(viewLifecycleOwner) { event ->
            when (event) {

                is SubCategoryPageEvents.NavigateToMediaFiles -> {
                    val action =
                        SubCategoryFragmentDirections.actionMainCategoryFragmentToAudioFilesFragment(
                            event.subCategoryId
                        )
                    findNavController().navigate(action)
                }

                is SubCategoryPageEvents.LoadSubCategories -> {
                    subCategoryAdapter.submitList(event.subCategories)
                }
            }
        }
    }

    override fun onItemClick(subCategoryWithMediaFile: SubCategoryWithMediaFile) {
        subCategoryViewModel.onEvents(
            SubCategoryViewModelEvents.OnNavigateToMediaFiles(
                subCategoryWithMediaFile
            )
        )
    }

//    private fun startService() {
//        ContextCompat.startForegroundService(
//            requireContext(),
//            Intent(requireContext(), MediaBrowserService::class.java)
//        )
//    }


}