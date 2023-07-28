package com.hossainehs.palplayer.presentation.home_page

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.hossainehs.palplayer.R
import com.hossainehs.palplayer.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {
    private lateinit var binding: FragmentHomeBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)

        binding.apply {

        }
    }
}