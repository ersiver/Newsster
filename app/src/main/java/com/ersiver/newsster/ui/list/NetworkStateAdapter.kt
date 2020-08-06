package com.ersiver.newsster.ui.list

import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter


class NetworkStateAdapter(private val adapter: ArticleAdapter): LoadStateAdapter<NetworkStateViewHolder>(){
    override fun onBindViewHolder(holder: NetworkStateViewHolder, loadState: LoadState) {
       holder.bind(loadState) { adapter.retry() }
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): NetworkStateViewHolder {
       return NetworkStateViewHolder.from(parent)
    }
}
