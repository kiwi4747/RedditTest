package com.example.reddittest.ui.main.data

import com.example.reddittest.ui.main.data.model.RedditQueryThread
import com.example.reddittest.ui.main.data.network.SearchInterface
import com.example.reddittest.ui.main.data.network.ServiceBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

object RemoteDataSource : DataSource {

    private val BASEPATH_REDDIT = "https://www.reddit.com/"
    private var redditInterface: SearchInterface =
        ServiceBuilder.buildService(SearchInterface::class.java, BASEPATH_REDDIT)

    override suspend fun searchByQuery(query: String): Flow<List<RedditQueryThread>> {
        val response = withContext(Dispatchers.IO) {
            redditInterface.getPositionByZip(query)
        }
        return flow { emit(response.body()?.data?.children ?: listOf<RedditQueryThread>()) }
    }

}