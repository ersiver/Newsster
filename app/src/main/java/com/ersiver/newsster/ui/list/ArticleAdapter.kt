package com.ersiver.newsster.ui.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ersiver.newsster.databinding.ArticleItemBinding
import com.ersiver.newsster.model.Article
import com.ersiver.newsster.util.formatTitle
import com.ersiver.newsster.util.loadImageOrDefault
import com.ersiver.newsster.util.loadOrGone

/**
 * Adapter for the grid of articles.
 */
class ArticleAdapter :
    PagingDataAdapter<Article, ArticleViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = getItem(position) ?: return
        holder.bind(article)
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

            setOnClickListener(articleItem)
        }
    }

    private fun setOnClickListener(articleItem: Article) {
        binding.detailItem.setOnClickListener{view ->
            navigateToDetail(articleItem, view)
        }
    }

    private fun navigateToDetail(articleItem: Article, view: View) {
        val directions = ArticleListFragmentDirections.actionArticleListFragmentToArticleFragment(articleItem)
        view.findNavController().navigate(directions)
    }

    companion object {
        fun from(parent: ViewGroup): ArticleViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ArticleItemBinding.inflate(layoutInflater, parent, false)
            return ArticleViewHolder(binding)
        }
    }
}