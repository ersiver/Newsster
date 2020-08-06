package com.ersiver.newsster.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadState.Error
import androidx.paging.LoadState.Loading
import androidx.recyclerview.widget.RecyclerView
import com.ersiver.newsster.databinding.NetworkStateItemBinding


/**
 * A View Holder that can display a progress indicator
 * click action button or error message depending o
 * network state of paging.
 */
class NetworkStateViewHolder(
    private val binding: NetworkStateItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(loadState: LoadState, retryCallback: () -> Unit) {
        binding.run {
            if (loadState is Error)
                errorText.text = loadState.error.localizedMessage

            progress.isVisible = loadState is Loading
            errorText.isVisible = loadState  is Error
            retryButton.isVisible = loadState is Error
            retryButton.setOnClickListener { retryCallback() }
        }
    }

    companion object {
        fun from(parent: ViewGroup): NetworkStateViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = NetworkStateItemBinding.inflate(layoutInflater,parent, false)
            return NetworkStateViewHolder(binding)

        }
    }
}