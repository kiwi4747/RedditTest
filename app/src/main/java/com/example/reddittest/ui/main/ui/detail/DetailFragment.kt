package com.example.reddittest.ui.main.ui.detail

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.lifecycle.whenResumed
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.reddittest.R
import com.example.reddittest.databinding.DetailFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailFragment : Fragment() {
    private val viewModel: DetailViewModel by viewModels()

    private var _binding: DetailFragmentBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DetailFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.redditThread?.let {
            context?.let { ctx ->
                // val url = if(it.data?.url.isNullOrEmpty()) it.data?.thumbnail else it.data?.url
                Glide.with(ctx)
                    .load(it.data?.url)
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
                    .into(binding.detailImage)
                /*val bitmap: Bitmap? =
                    Glide
                        .with(ctx)
                        .asBitmap()
                        .load(it.data?.url)
                        .submit()
                        .get()*/
                viewModel.setImage(it.data?.url)
            }
        }
        binding.detailImage.setOnClickListener {
            viewModel.saveImage()
        }


        lifecycleScope.launchWhenResumed {
            viewModel.outputWorkInfos.collect {
                if (it.isNotEmpty()) {
                    if (it[0].state.isFinished) {
                        Toast.makeText(context, "PHOTO SAVED", Toast.LENGTH_LONG).show()

                        binding.progressBar.visibility = View.GONE
                    } else {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}