package com.example.reddittest.ui.main.ui.landing

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.reddittest.R
import com.example.reddittest.databinding.LandingFragmentBinding
import com.example.reddittest.ui.main.ui.adapter.SearchImagesAdapter
import com.example.reddittest.ui.main.utils.onQueryTextChanged
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LandingFragment : Fragment() {

    private var _binding: LandingFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LandingViewModel by viewModels()

    private lateinit var searchView: SearchView
    private var adapter: SearchImagesAdapter = SearchImagesAdapter()
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

        adapter.clickCallback = {
            val action = LandingFragmentDirections.actionLandingFragmentToDetailFragment(
                it
            )
            findNavController().navigate(action)
        }
        binding.landingRecycler.apply {
            layoutManager = GridLayoutManager(context, 2)
            setAdapter(this@LandingFragment.adapter)
        }
        /* lifecycleScope.launchWhenResumed {
             viewModel.searchFlow.collect {
               //  (binding.landingRecycler.adapter as SearchImagesAdapter).list = it
             }
         }*/
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
            viewModel.currentQueryValue = it
            search(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}