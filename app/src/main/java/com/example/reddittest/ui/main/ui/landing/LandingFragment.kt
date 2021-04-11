package com.example.reddittest.ui.main.ui.landing

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.example.reddittest.R
import com.example.reddittest.databinding.LandingFragmentBinding
import com.example.reddittest.ui.main.ui.adapter.RedditThreadLoadStateAdapter
import com.example.reddittest.ui.main.ui.adapter.RedditThreadGalleryAdapter
import com.example.reddittest.ui.main.utils.hideKeyboard
import com.example.reddittest.ui.main.utils.onQueryTextChanged
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LandingFragment : Fragment() {

    private var _binding: LandingFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LandingViewModel by viewModels()

    private lateinit var searchView: SearchView
    private var adapter: RedditThreadGalleryAdapter = RedditThreadGalleryAdapter()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = LandingFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        initAdapter()

        // Scroll to top when the list is refreshed from network.
        lifecycleScope.launch {
            adapter.loadStateFlow
                // Only emit when REFRESH LoadState for RemoteMediator changes.
                .distinctUntilChangedBy { it.refresh }
                // Only react to cases where Remote REFRESH completes i.e., NotLoading.
                .filter { it.refresh is LoadState.NotLoading }
                .collect { binding.landingRecycler.scrollToPosition(0) }
        }
    }

    private fun initAdapter() {
        adapter.clickCallback = {
            val action = LandingFragmentDirections.actionLandingFragmentToDetailFragment(
                it
            )
            findNavController().navigate(action)
        }

        adapter.addLoadStateListener { loadState ->
            // show empty list
            val isListEmpty = loadState.refresh is LoadState.NotLoading && adapter.itemCount == 0
            showEmptyList(isListEmpty)

            // Only show the list if refresh succeeds.
            binding.landingRecycler.isVisible = loadState.source.refresh is LoadState.NotLoading
            // Show loading spinner during initial load or refresh.
            binding.progressBar.isVisible = loadState.source.refresh is LoadState.Loading
            // Show the retry state if initial load or refresh fails.
            binding.retryButton.isVisible = loadState.source.refresh is LoadState.Error

            // Toast on any error, regardless of whether it came from RemoteMediator or PagingSource
            val errorState = loadState.source.append as? LoadState.Error
                ?: loadState.source.prepend as? LoadState.Error
                ?: loadState.append as? LoadState.Error
                ?: loadState.prepend as? LoadState.Error
            errorState?.let {
                Toast.makeText(
                    context,
                    "\uD83D\uDE28 Wooops ${it.error}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }


        binding.landingRecycler.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = this@LandingFragment.adapter.withLoadStateFooter(
                footer = RedditThreadLoadStateAdapter { this@LandingFragment.adapter.retry() }
            )
        }

        /*adapter.withLoadStateHeaderAndFooter(
            header = RedditThreadLoadStateAdapter { adapter.retry() },
            footer = RedditThreadLoadStateAdapter { adapter.retry() }
        )*/
        binding.retryButton.setOnClickListener { adapter.retry() }
    }

    private fun showEmptyList(isEmptyList: Boolean) {
        binding.emptyList.isVisible = isEmptyList
        binding.landingRecycler.isVisible = !isEmptyList
    }

    private var searchJob: Job? = null

    private fun search(query: String) {
        // Make sure we cancel the previous job before creating a new one
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            viewModel.searchRepo(query).collectLatest {
                adapter.submitData(it)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_options, menu)

        val searchItem = menu.findItem(R.id.action_search)
        searchView = searchItem.actionView as SearchView

        val pendingQuery = viewModel.currentQueryValue
        if (pendingQuery != null && pendingQuery.isNotEmpty()) {
            searchItem.expandActionView()
            searchView.setQuery(pendingQuery, false)
        }

        searchView.onQueryTextChanged {
            search(it)
            hideKeyboard()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}