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
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class LandingFragment : Fragment() {

    private var _binding: LandingFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LandingViewModel by viewModels()

    private lateinit var searchView: SearchView

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
        binding.button.setOnClickListener {
            findNavController().navigate(R.id.action_landingFragment_to_detailFragment)
        }

        binding.landingRecycler.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = context?.let {
                SearchImagesAdapter().apply {
                    clickCallback = {
                        /*Navigation.findNavController(view)
                            .navigate(R.id.action_mainFragment_to_galleryFragment, Bundle().apply {
                                putSerializable(EXTRA_LISTA, list as? Serializable)
                                putSerializable(EXTRA_CHILD, child)
                            }*/
                    }
                }
            }
        }
        lifecycleScope.launchWhenResumed {
            viewModel.searchFlow.collect {
                (binding.landingRecycler.adapter as SearchImagesAdapter).list = it
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_options, menu)

        val searchItem = menu.findItem(R.id.action_search)
        searchView = searchItem.actionView as SearchView

        val pendingQuery = viewModel.searchQuery.value
        if (pendingQuery != null && pendingQuery.isNotEmpty()) {
            searchItem.expandActionView()
            searchView.setQuery(pendingQuery, false)
        }

        searchView.onQueryTextChanged {
            viewModel.searchQuery.value = it
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}