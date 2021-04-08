package com.example.reddittest.ui.main.data

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject


class RedditRepository @Inject constructor(@ApplicationContext private val context: Context) {

    fun searchByName(query:String): Flow<String> {
        return flowOf("ah","ciao")    }
}