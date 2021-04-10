package com.example.reddittest.ui.main.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.reddittest.R
import com.example.reddittest.databinding.RowSearchImageBinding
import com.example.reddittest.ui.main.data.model.RedditQueryThread

class SearchImagesAdapter :
    PagingDataAdapter<RedditQueryThread, SearchImagesAdapter.PhotoViewHolder>(
        REDDIT_THREAD_COMPARATOR
    ) {

    private var _binding: RowSearchImageBinding? = null
    private val binding get() = _binding!!
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        _binding = RowSearchImageBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PhotoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {

        val currentItem = getItem(position)

        if (currentItem != null) {
            holder.bind(currentItem, clickCallback)
        }

    }

    class PhotoViewHolder(private val binding: RowSearchImageBinding) :
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


    var clickCallback: ((photo: RedditQueryThread) -> Unit)? = null

    companion object {
        private val REDDIT_THREAD_COMPARATOR = object : DiffUtil.ItemCallback<RedditQueryThread>() {
            override fun areItemsTheSame(
                oldItem: RedditQueryThread,
                newItem: RedditQueryThread
            ): Boolean =
                oldItem.data?.name == newItem.data?.name

            override fun areContentsTheSame(
                oldItem: RedditQueryThread,
                newItem: RedditQueryThread
            ): Boolean =
                oldItem == newItem
        }
    }
}
