package com.example.reddittest.ui.main.data

import android.content.Context
import com.example.reddittest.ui.main.data.model.RedditQueryThread
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class RedditRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val remoteDataSource: DataSource
) : IDataRepository {

    override suspend fun searchByQuery(query: String): Flow<List<RedditQueryThread>> {
        return remoteDataSource.searchByQuery(query)
    }
}