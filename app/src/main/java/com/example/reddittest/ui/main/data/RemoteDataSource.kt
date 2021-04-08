package com.example.reddittest.ui.main.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

object RemoteDataSource : DataSource {
    override fun searchByQuery(query: String): Flow<String> {
        return flowOf("boh")
    }
}