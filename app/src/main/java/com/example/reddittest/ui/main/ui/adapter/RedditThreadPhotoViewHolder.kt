package com.example.reddittest.ui.main.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.reddittest.R
import com.example.reddittest.databinding.RowSearchImageBinding
import com.example.reddittest.ui.main.data.model.RedditQueryThread


class RedditThreadPhotoViewHolder(private val binding: RowSearchImageBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(data: RedditQueryThread, clickCallback: ((RedditQueryThread) -> Unit)? = null) {
        binding.apply {
            Glide.with(itemView)
                .load(data.data?.thumbnail)
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
                .error(R.drawable.ic_close)
                .into(rowGalleryImage)

            rowGalleryTitle.text = data.data?.title
            binding.root.setOnClickListener { clickCallback?.invoke(data) }
        }
    }
}