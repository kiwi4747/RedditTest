package com.example.reddittest.ui.main.data

import androidx.paging.PagingData
import com.example.reddittest.ui.main.data.model.RedditQueryThread
import kotlinx.coroutines.flow.Flow

interface IDataRepository {
    fun searchByQuery(query: String): Flow<PagingData<RedditQueryThread>>
}