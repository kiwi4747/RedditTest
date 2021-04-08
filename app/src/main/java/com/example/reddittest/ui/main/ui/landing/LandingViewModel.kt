package com.example.reddittest.ui.main.ui.landing

import androidx.hilt.Assisted
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import com.example.reddittest.ui.main.data.IDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class LandingViewModel @Inject constructor(
    private val repository : IDataRepository,
    @Assisted private val state: SavedStateHandle
): ViewModel() {

    val searchQuery = state.getLiveData("searchQuery", "")

    val searchFlow = searchQuery.asFlow<String>().flatMapLatest {query->
        repository.searchByQuery(query)
    }
}