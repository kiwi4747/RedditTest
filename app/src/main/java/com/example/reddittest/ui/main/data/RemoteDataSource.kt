package com.example.reddittest.ui.main.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.reddittest.ui.main.data.model.RedditQueryThread
import com.example.reddittest.ui.main.data.network.SearchInterface
import com.example.reddittest.ui.main.data.network.ServiceBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

object RemoteDataSource : DataSource {

    private const val BASEPATH_REDDIT = "https://www.reddit.com/"
    private var redditInterface =
        ServiceBuilder.buildService(SearchInterface::class.java, BASEPATH_REDDIT)

    override fun searchByQuery(query: String): Flow<PagingData<RedditQueryThread>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { RedditQueryPagingSource(redditInterface, query) }
        ).flow
    }

}