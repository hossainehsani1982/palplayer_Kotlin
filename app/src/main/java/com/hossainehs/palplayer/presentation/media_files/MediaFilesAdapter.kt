package com.hossainehs.palplayer.presentation.media_files

import android.animation.Animator
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hossainehs.palplayer.databinding.MediaFileItemBinding
import com.hossainehs.palplayer.domain.model.MediaFile

class MediaFilesAdapter constructor(
    private val listener: MediaFilesAdapterListener
) : ListAdapter<MediaFile, MediaFilesAdapter.MediaFilesViewHolder>(MediaFilesDiffUtil()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaFilesViewHolder {
        val binding = MediaFileItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MediaFilesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MediaFilesViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    inner class MediaFilesViewHolder(private val binding: MediaFileItemBinding) :
        RecyclerView.ViewHolder(
            binding.root
        ) {
        fun bind(mediaFile: MediaFile) {
            itemView.apply {
                binding.apply {
                    siArtwork.setImageURI(Uri.parse(mediaFile.artWorkUri))
                    tvMediaTitle.text = mediaFile.displayName
                    tvArtist.text = mediaFile.artist
                    tvFileDuration.text = formatTime(mediaFile.duration)
                    ltBtnPlay.apply {
                        if (!mediaFile.isPlaying) {
                            setMinAndMaxFrame(0, 0)
                            playAnimation()
                        } else {
                            setMinAndMaxFrame(0, 33)
                            playAnimation()
                        }

                    }
                    root.setOnClickListener {
                        ltBtnPlay.apply {
                            if (!mediaFile.isPlaying) {
                                setMinAndMaxFrame(0, 0)
                                playAnimation()


                            } else {
                                setMinAndMaxFrame(0, 33)
                                playAnimation()
                            }

                        }
                        listener.onBtnPlayPauseClicked(mediaFile)
                    }


                }
            }
        }
    }

    class MediaFilesDiffUtil : DiffUtil.ItemCallback<MediaFile>() {
        override fun areItemsTheSame(
            oldItem: MediaFile,
            newItem: MediaFile
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: MediaFile,
            newItem: MediaFile
        ): Boolean {
            return oldItem.audioFileId == newItem.audioFileId
        }
    }

    interface MediaFilesAdapterListener {
        fun onBtnPlayPauseClicked(mediaFile: MediaFile)
    }

    private fun formatTime(milliseconds: Long): String {
        val seconds = (milliseconds / 1000).toInt()
        val hours = seconds / 3600
        val minutes = (seconds % 3600) / 60
        val remainingSeconds = seconds % 60

        return String.format("%02d:%02d:%02d", hours, minutes, remainingSeconds)
    }

}

//                                this.addAnimatorListener(object : Animator.AnimatorListener{
//                                    override fun onAnimationStart(p0: Animator) {}
//                                    override fun onAnimationEnd(p0: Animator) {
//                                    }
//                                    override fun onAnimationCancel(p0: Animator) {}
//
//                                    override fun onAnimationRepeat(p0: Animator) {}
//
//                                })