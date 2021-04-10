package com.example.reddittest.ui.main.data.network

import com.example.reddittest.ui.main.data.model.RedditQueryResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SearchInterface {
    @GET("r/{keyword}/new.json")
    suspend fun searchThreadByQueryNew(
        @Path("keyword") keyword: String?,
        @Query("limit") elementPerPage: Int,
        @Query("after") after: String? = null
    ): RedditQueryResponse

    @GET("r/{keyword}/top.json")
    suspend fun searchThreadByQueryTop(
        @Path("keyword") keyword: String?,
        @Query("limit") elementPerPage: Int,
        @Query("after") after: String? = null
    ): Response<RedditQueryResponse>
}