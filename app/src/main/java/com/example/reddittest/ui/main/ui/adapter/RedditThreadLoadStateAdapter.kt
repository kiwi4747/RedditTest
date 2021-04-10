package com.example.reddittest.ui.main.ui.adapter

import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter

class RedditThreadLoadStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<RedditThreadLoadStateViewHolder>() {
    override fun onBindViewHolder(holder: RedditThreadLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): RedditThreadLoadStateViewHolder {
        return RedditThreadLoadStateViewHolder.create(parent, retry)
    }
}