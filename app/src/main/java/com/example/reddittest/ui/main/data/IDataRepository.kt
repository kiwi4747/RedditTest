package com.example.reddittest.ui.main.data

import kotlinx.coroutines.flow.Flow

interface IDataRepository {
    fun searchByQuery(query: String): Flow<String>
}