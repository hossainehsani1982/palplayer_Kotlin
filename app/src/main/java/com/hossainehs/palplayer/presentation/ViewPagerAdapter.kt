package com.hossainehs.palplayer.presentation

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.hossainehs.palplayer.presentation.home_page.HomeFragment
import com.hossainehs.palplayer.presentation.hub.HubFragment

// create list adapter

class ViewPagerAdapter(activity : AppCompatActivity)
    : FragmentStateAdapter(activity){
    override fun getItemCount(): Int {
       return 5
    }


    override fun createFragment(position: Int): Fragment {

        return when (position){
            0 -> {
                HomeFragment()
            }
            else -> {
                HubFragment()
            }

        }
    }
}