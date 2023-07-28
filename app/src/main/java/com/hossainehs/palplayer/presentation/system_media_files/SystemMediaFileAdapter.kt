package com.hossainehs.palplayer.presentation.system_media_files


import android.net.Uri
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hossainehs.palplayer.databinding.FilesOnDeviceItemBinding
import com.hossainehs.palplayer.domain.model.SystemMediaFile

@RequiresApi(Build.VERSION_CODES.Q)
class SystemMediaFileAdapter(
    private val listener: OnItemClickListener,
) : ListAdapter<SystemMediaFile, SystemMediaFileAdapter.SystemMediaFileViewHolder>(
    SystemMediaFileDiffUtil()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SystemMediaFileViewHolder {
        val binding = FilesOnDeviceItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SystemMediaFileViewHolder(binding)
    }


    override fun onBindViewHolder(holder: SystemMediaFileViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    inner class SystemMediaFileViewHolder(private val binding: FilesOnDeviceItemBinding) :
        RecyclerView.ViewHolder(
            binding.root
        ) {

        fun bind(systemMediaFile: SystemMediaFile) {
            itemView.apply {
                binding.apply {
                    val mContext = tvSystemArtist.context
                    tvSystemMediaTitle.text = systemMediaFile.fileName
                    tvSystemFileDuration.text = systemMediaFile.duration.toString()
                    tvSystemArtist.text = systemMediaFile.artist
                    println(systemMediaFile.contentUri)
                    siSystemArtwork.setImageURI(Uri.parse(systemMediaFile.contentUri))

                    if (systemMediaFile.isSelected) {
                        root.background =
                            AppCompatResources.getDrawable(
                                mContext,
                                android.R.color.holo_blue_bright
                            )
                    } else {
                        root.background = AppCompatResources.getDrawable(
                            mContext,
                            android.R.color.transparent
                        )
                    }
                    root.setOnClickListener {
                        systemMediaFile.isSelected = !systemMediaFile.isSelected
                        if (systemMediaFile.isSelected) {
                            root.background =
                                AppCompatResources.getDrawable(
                                    mContext,
                                    android.R.color.holo_blue_bright
                                )
                            listener.onItemSelected(systemMediaFile)
                        } else {
                            root.background = AppCompatResources.getDrawable(
                                mContext,
                                android.R.color.transparent
                            )
                            listener.onItemUnSelected(systemMediaFile)
                        }

                    }
                }
            }
        }
    }

    class SystemMediaFileDiffUtil : DiffUtil.ItemCallback<SystemMediaFile>() {
        override fun areItemsTheSame(
            oldItem: SystemMediaFile,
            newItem: SystemMediaFile
        ): Boolean {
            return oldItem.fileId == newItem.fileId
        }

        override fun areContentsTheSame(
            oldItem: SystemMediaFile,
            newItem: SystemMediaFile
        ): Boolean {
            return oldItem == newItem
        }
    }

    interface OnItemClickListener {
        fun onItemSelected(systemMediaFile: SystemMediaFile)
        fun onItemUnSelected(systemMediaFile: SystemMediaFile)

    }


}

