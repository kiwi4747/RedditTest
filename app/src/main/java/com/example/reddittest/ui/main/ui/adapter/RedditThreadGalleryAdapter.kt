package com.example.reddittest.ui.main.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.reddittest.databinding.RowSearchImageBinding
import com.example.reddittest.ui.main.data.model.RedditQueryThread

class RedditThreadGalleryAdapter :
    PagingDataAdapter<RedditQueryThread, RedditThreadPhotoViewHolder>(
        REDDIT_THREAD_COMPARATOR
    ) {

    private var _binding: RowSearchImageBinding? = null
    private val binding get() = _binding!!
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RedditThreadPhotoViewHolder {
        _binding = RowSearchImageBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RedditThreadPhotoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RedditThreadPhotoViewHolder, position: Int) {

        val currentItem = getItem(position)

        if (currentItem != null) {
            holder.bind(currentItem, clickCallback)
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
