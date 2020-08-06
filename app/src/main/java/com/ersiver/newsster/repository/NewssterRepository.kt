package com.ersiver.newsster.repository

import androidx.lifecycle.LiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.ersiver.newsster.api.NewssterService
import com.ersiver.newsster.db.NewssterDatabase
import com.ersiver.newsster.model.Article
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Repository implementation that uses a database backed [androidx.paging.PagingSource] and
 * [androidx.paging.RemoteMediator] to load pages from network when there are no more items cached
 * in the database to load.
 */

class NewssterRepository @Inject constructor(
    private val service: NewssterService,
    private val database: NewssterDatabase
) {

    /**
     * Search articles, exposed as a stream of data that will emit
     * every time we get more data from the network.
     */
    @ExperimentalPagingApi
    fun fetchArticles(language: String, category: String): Flow<PagingData<Article>> {
        val pagingSourceFactory =
            { database.articleDao().getNews(language,category) }

        return Pager(
            config = PagingConfig(NETWORK_PAGE_SIZE, maxSize = 300, enablePlaceholders = true),
            remoteMediator = NewssterRemoteMediator(language, category, service, database),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    fun getArticle(id: String): LiveData<Article> {
        return database.articleDao().getArticle(id)
    }

    companion object {
        private const val NETWORK_PAGE_SIZE = 80
    }
}