package com.hossainehs.palplayer.presentation.hub

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.hossainehs.palplayer.R
import com.hossainehs.palplayer.databinding.FragmentHubBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class HubFragment : Fragment(R.layout.fragment_hub) {
    private lateinit var binding: FragmentHubBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHubBinding.bind(view)

    }

}