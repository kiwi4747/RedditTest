package com.example.reddittest.ui.main.data

import kotlinx.coroutines.flow.Flow


interface DataSource {

    //create
    //read
    //update
    //delete
    fun searchByQuery(query: String): Flow<String>
}