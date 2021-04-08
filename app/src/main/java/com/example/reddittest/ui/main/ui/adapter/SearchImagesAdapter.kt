package com.example.reddittest.ui.main.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.reddittest.R
import com.example.reddittest.databinding.RowSearchImageBinding
import com.example.reddittest.ui.main.data.model.RedditQueryThread

class SearchImagesAdapter : RecyclerView.Adapter<SearchImagesAdapter.ViewHolder>() {

    var list: List<RedditQueryThread> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private var _binding: RowSearchImageBinding? = null
    private val binding get() = _binding!!
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        _binding = RowSearchImageBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        list[position].let { item ->
            if (item.data?.thumbnail != null) {
                Glide.with(binding.root.context)
                    .load(item.data.thumbnail)
                    .transform(
                        CenterCrop(),
                        RoundedCorners(
                            binding.root.resources.getDimensionPixelSize(
                                R.dimen.corner_radius
                            )
                        )
                    )
                    .placeholder(R.drawable.ic_cached)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .apply(
                        RequestOptions()
                    )
                    .fallback(R.drawable.ic_close)
                    .into(binding.rowGalleryImage)
            } else {
                binding.rowGalleryImage.setImageResource(R.drawable.ic_close)
            }
            binding.rowGalleryTitle.text = item.data?.title
            binding.root.setOnClickListener { clickCallback?.invoke(item) }
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    var clickCallback: ((photo: RedditQueryThread) -> Unit)? = null

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
