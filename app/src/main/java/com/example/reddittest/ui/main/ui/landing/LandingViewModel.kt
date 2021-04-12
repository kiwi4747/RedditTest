package com.example.reddittest.ui.main.ui.landing

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LandingViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val repository: IDataRepository
) : ViewModel() {

    private var _currentQueryValue = MutableLiveData("")
    val currentQueryValue: LiveData<String> = _currentQueryValue
    fun setCurrentQuery(queryString: String) {
        CoroutineScope(Dispatchers.IO).launch {
            userPreferencesRepository.setLastQuery(queryString)
        }
    }

    private var currentSearchResult: Flow<PagingData<RedditQueryThread>>? = null
    private var lastQuery = ""

    init {
        viewModelScope.launch {
            userPreferencesRepository.lastQueryFlow.collectLatest {
                _currentQueryValue.postValue(it)
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