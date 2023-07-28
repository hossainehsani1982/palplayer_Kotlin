package com.hossainehs.palplayer.presentation


import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.hossainehs.palplayer.databinding.ActivityMainBinding
import com.hossainehs.palplayer.domain.sharedPreferences.Preferences
import com.hossainehs.palplayer.service.MusicService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    @Inject
    lateinit var preferences: Preferences


    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        val intent = Intent(this, MusicService::class.java)
//        startForegroundService(intent)



        val metric = Resources.getSystem().displayMetrics
        val layoutParams = binding.fragmentCommandCenter.layoutParams
        layoutParams.height = (metric.heightPixels * 0.8).toInt()

        if (isStoragePermissionGranted()) {
            CoroutineScope(Dispatchers.IO).launch {
                preferences.getPermissionStatus().collect {
                    if (!it) {
                        preferences.setPermissionStatus(true)
                    }
                }
            }
        }else{

            CoroutineScope(Dispatchers.IO).launch {
                preferences.getPermissionStatus().collect {
                    if (it) {
                        preferences.setPermissionStatus(false)
                    }
                }
            }
        }
        binding.apply {
            tabLayout.tabMode = TabLayout.MODE_SCROLLABLE
            viewPager.adapter = ViewPagerAdapter(this@MainActivity)

            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = when (position) {
                    0 -> "Home"
                    1 -> "Music"
                    2 -> "Audio Books"
                    3 -> "Recordings"
                    4 -> "Podcast"
                    else -> "home"
                }
            }.attach()



            tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    tab?.let {
                        println("mActivity, tab position: ${it.position}")
                        viewPager.currentItem = it.position
                        CoroutineScope(Dispatchers.IO).launch {
                            preferences.setCurrentFragmentPageNumber(it.position)
                        }
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                    // do nothing
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                    // do nothing
                }

            })

        }
    }


    private fun isStoragePermissionGranted(): Boolean {
        return if ((checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED)
            && checkSelfPermission(Manifest.permission.READ_MEDIA_AUDIO)
            == PackageManager.PERMISSION_GRANTED
            && checkSelfPermission(Manifest.permission.FOREGROUND_SERVICE)
            == PackageManager.PERMISSION_GRANTED
            && checkSelfPermission(Manifest.permission.FOREGROUND_SERVICE)
            == PackageManager.PERMISSION_GRANTED
            && checkSelfPermission(Manifest.permission.FOREGROUND_SERVICE_MEDIA_PLAYBACK)
            == PackageManager.PERMISSION_GRANTED
            && checkSelfPermission(
                Manifest.permission.MODIFY_AUDIO_SETTINGS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            true
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_MEDIA_AUDIO,
                    Manifest.permission.FOREGROUND_SERVICE,
                    Manifest.permission.POST_NOTIFICATIONS,
                    Manifest.permission.FOREGROUND_SERVICE_MEDIA_PLAYBACK,
                    Manifest.permission.MODIFY_AUDIO_SETTINGS
                ),
                1
            )
            false
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults.size != 0) {
                for (i in grantResults.indices) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED && grantResults[1]
                        != PackageManager.PERMISSION_GRANTED) {
                        isStoragePermissionGranted()
                    }
                }
            }
        }
    }




}