package com.example.reddittest.ui.main.data

import android.content.Context
import androidx.paging.PagingData
import com.example.reddittest.ui.main.data.model.RedditQueryThread
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import javax.inject.Inject


class RedditRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val remoteDataSource: DataSource
) : IDataRepository {

    override fun searchByQuery(query: String): Flow<PagingData<RedditQueryThread>> {
        return if(query.isNotEmpty()) remoteDataSource.searchByQuery(query) else emptyFlow()
    }
}