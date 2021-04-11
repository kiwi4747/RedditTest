package com.example.reddittest.ui.main.ui.landing

import androidx.hilt.Assisted
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.reddittest.ui.main.data.IDataRepository
import com.example.reddittest.ui.main.data.datastore.UserPreferencesRepository
import com.example.reddittest.ui.main.data.model.RedditQueryThread
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LandingViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val repository: IDataRepository,
    @Assisted private val state: SavedStateHandle
) : ViewModel() {

    private var _currentQueryValue = MutableStateFlow("")
    val currentQueryValue: MutableStateFlow<String> = _currentQueryValue
    fun setCurrentQuery(queryString: String) {
        CoroutineScope(Dispatchers.IO).launch {
            userPreferencesRepository.setLastQuery(queryString)
        }
    }

    private var currentSearchResult: Flow<PagingData<RedditQueryThread>>? = null
    private var lastQuery = ""

    init {
        viewModelScope.launch {
            userPreferencesRepository.lastQueryFlow.collect {
                _currentQueryValue.value = it
            }
        }
    }

    fun searchRepo(queryString: String): Flow<PagingData<RedditQueryThread>> {
        val lastResult = currentSearchResult
        if (queryString == lastQuery && lastResult != null) {
            return lastResult
        }
        lastQuery = queryString
        val newResult: Flow<PagingData<RedditQueryThread>> = repository.searchByQuery(queryString)
            .cachedIn(viewModelScope)
        currentSearchResult = newResult
        return newResult
    }
}