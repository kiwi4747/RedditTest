package com.example.reddittest.ui.main.data

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class RedditRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val remoteDataSource: DataSource
) : IDataRepository {

    override fun searchByQuery(query: String): Flow<String> {
        return remoteDataSource.searchByQuery(query)
    }
}