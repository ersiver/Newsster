package com.ersiver.newsster.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ersiver.newsster.databinding.ArticleItemBinding
import com.ersiver.newsster.model.Article
import com.ersiver.newsster.util.formatTitle
import com.ersiver.newsster.util.loadImageOrDefault
import com.ersiver.newsster.util.loadOrGone

/**
 * View Holder for a [Article] RecyclerView list item.
 */
class ArticleViewHolder(private val binding: ArticleItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(articleItem: Article) {
        binding.run {
            source.loadOrGone(articleItem.source.name)
            title.loadOrGone(articleItem.title.formatTitle())
            articleImage.loadImageOrDefault(articleItem.imgUrl)
        }
    }

    companion object {
        fun from(parent: ViewGroup): ArticleViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ArticleItemBinding.inflate(layoutInflater, parent, false)
            return ArticleViewHolder(binding)
        }
    }
}