package com.ersiver.newsster.ui.list

import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.ersiver.newsster.model.Article

/**
 * Adapter for the grid of articles.
 */
class ArticleAdapter(private val listener: ArticleViewClick) :
    PagingDataAdapter<Article, ArticleViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = getItem(position) ?: return

        holder.itemView.setOnClickListener{
            listener.onClick(holder.itemView, article)
        }
        holder.bind(article)
    }

    interface ArticleViewClick {
        fun onClick(view: View, article: Article)
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<Article> =
            object : DiffUtil.ItemCallback<Article>() {
                override fun areItemsTheSame(oldItem: Article, newItem: Article) =
                    oldItem == newItem

                override fun areContentsTheSame(oldItem: Article, newItem: Article) =
                    oldItem.id == newItem.id
            }
    }
}