package com.example.reddittest.ui.main.ui.detail

import androidx.hilt.Assisted
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.reddittest.ui.main.data.model.RedditQueryThread
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(@Assisted private val state: SavedStateHandle):ViewModel(){

    val redditThread = state.get<RedditQueryThread>("reddit_thread")
}