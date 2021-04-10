package com.example.reddittest.ui.main.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.reddittest.ui.main.data.model.RedditQueryThread
import com.example.reddittest.ui.main.data.network.SearchInterface
import retrofit2.HttpException
import java.io.IOException

class RedditQueryPagingSource(
    private val service: SearchInterface,
    private val query: String
) : PagingSource<String, RedditQueryThread>() {

    private var lastKey: String? = null
    override suspend fun load(params: LoadParams<String>): LoadResult<String, RedditQueryThread> {
        val position = params.key
        val apiQuery = query
        return try {
            val response = service.getPositionByZip(apiQuery, 10, position)
            val repos = response.body()?.data?.children ?: emptyList()
            val nextKey = if (repos.isNullOrEmpty()) {
                null
            } else {
                response.body()?.data?.after
            }
            LoadResult.Page(repos, lastKey, nextKey)
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<String, RedditQueryThread>): String? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey
                ?: lastKey
        }//null

    }
}