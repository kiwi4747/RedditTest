package com.example.reddittest.ui.main.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.work.WorkInfo
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.reddittest.R
import com.example.reddittest.databinding.DetailFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

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

        val args: DetailFragmentArgs by navArgs()
        viewModel.bindData(args.redditThread)

        viewModel.redditThread?.let {
            if (it.data?.url?.endsWith(".jpg") == true)
                context?.let { ctx ->
                    // val url = if(it.data?.url.isNullOrEmpty()) it.data?.thumbnail else it.data?.url
                    Glide.with(ctx)
                        .load(it.data.url)
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
                    viewModel.setImage(it.data.url)
                }
            binding.detailButtonDownload.isVisible = it.data?.url?.endsWith(".jpg") == true
            binding.detailTitle.text = it.data?.title
        }
        binding.detailButtonDownload.setOnClickListener {
            viewModel.saveImage()
        }


        viewModel.outputWorkInfos.observe(viewLifecycleOwner,
            Observer<List<WorkInfo>> { info ->
                if (info?.isNotEmpty() == true) {
                    if (info[0].state.isFinished) {
                        binding.progressBar.visibility = View.GONE
                        binding.detailButtonDownload.setImageResource(R.drawable.ic_download_done)
                    } else {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                }
            })


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}