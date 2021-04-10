package com.example.reddittest.ui.main.ui.landing

import androidx.hilt.Assisted
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.reddittest.ui.main.data.IDataRepository
import com.example.reddittest.ui.main.data.model.RedditQueryThread
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LandingViewModel @Inject constructor(
    private val repository: IDataRepository,
    @Assisted private val state: SavedStateHandle
) : ViewModel() {

    // val searchQuery = state.getLiveData("searchQuery", "")
//
    // val searchFlow = searchQuery.asFlow<String>().flatMapLatest {query->
    //     repository.searchByQuery(query)
    // }


    var currentQueryValue: String? = state.getLiveData("searchQuery", "").value

    private var currentSearchResult: Flow<PagingData<RedditQueryThread>>? = null

    fun searchRepo(queryString: String): Flow<PagingData<RedditQueryThread>> {
        val lastResult = currentSearchResult
        if (queryString == currentQueryValue && lastResult != null) {
            return lastResult
        }
        currentQueryValue = queryString
        val newResult: Flow<PagingData<RedditQueryThread>> = repository.searchByQuery(queryString)
            .cachedIn(viewModelScope)
        currentSearchResult = newResult
        return newResult
    }
}