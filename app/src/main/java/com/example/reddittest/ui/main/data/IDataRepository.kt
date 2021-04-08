package com.example.reddittest.ui.main.data

import com.example.reddittest.ui.main.data.model.RedditQueryThread
import kotlinx.coroutines.flow.Flow

interface IDataRepository {
    suspend fun searchByQuery(query: String): Flow<List<RedditQueryThread>>
}