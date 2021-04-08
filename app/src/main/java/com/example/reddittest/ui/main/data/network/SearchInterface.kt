package com.example.reddittest.ui.main.data.network

import com.example.reddittest.ui.main.data.model.RedditQueryResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface SearchInterface {
    @GET("r/{keyword}/top.json")
    suspend fun getPositionByZip(@Path("keyword") keyword: String?): Response<RedditQueryResponse>
}