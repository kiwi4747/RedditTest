package com.example.reddittest.ui.main.data

import com.example.reddittest.ui.main.data.model.RedditQueryThread
import kotlinx.coroutines.flow.Flow


interface DataSource {

    //create
    //read
    //update
    //delete
    suspend fun searchByQuery(query: String): Flow<List<RedditQueryThread>>
}