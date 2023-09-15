package com.hossainehs.palplayer.presentation.sub_category

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.hossainehs.palplayer.domain.model.Relation.SubCategoryWithMediaFile
import com.hossainehs.palplayer.databinding.SubCategoryItemBinding

class SubCategoryAdapter(private val listener: OnItemClickListener) :
    ListAdapter<SubCategoryWithMediaFile, SubCategoryAdapter.MainCategoryViewHolder>(
        MainCategoryDiffCallback()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainCategoryViewHolder {
        val binding = SubCategoryItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MainCategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainCategoryViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    inner class MainCategoryViewHolder(private val binding: SubCategoryItemBinding) :
        RecyclerView.ViewHolder(
            binding.root
        ) {
        fun bind(subCategoryWithMediaFile: SubCategoryWithMediaFile) {
            itemView.apply {
                binding.apply {
                    tvSubCategoryTitle.text = subCategoryWithMediaFile.subCategory.name
                    tvFilesCount.text = subCategoryWithMediaFile.mediaFiles.size.toString()
                    ivNavigateToMediaFiles.setOnClickListener {
                        listener.onItemClick(subCategoryWithMediaFile)
                    }
                }
            }
        }
    }

    class MainCategoryDiffCallback : DiffUtil.ItemCallback<SubCategoryWithMediaFile>() {
        override fun areItemsTheSame(
            oldItem: SubCategoryWithMediaFile,
            newItem: SubCategoryWithMediaFile
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: SubCategoryWithMediaFile,
            newItem: SubCategoryWithMediaFile
        ): Boolean {
            return oldItem.subCategory.subCategoryId == newItem.subCategory.subCategoryId
        }
    }

    interface OnItemClickListener {
        fun onItemClick(subCategoryWithMediaFile: SubCategoryWithMediaFile)
    }


}