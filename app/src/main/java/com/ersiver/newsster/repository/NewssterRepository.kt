package com.ersiver.newsster.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.ersiver.newsster.api.NewssterService
import com.ersiver.newsster.db.NewssterDatabase
import com.ersiver.newsster.model.Article
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository implementation that uses a database backed
 * [androidx.paging.PagingSource] and [androidx.paging.RemoteMediator]
 * to load pages from network when there are no more items cached
 * in the database to load.
 */

@Singleton
class NewssterRepository @Inject constructor(
    private val service: NewssterService,
    private val database: NewssterDatabase
) {

    @ExperimentalPagingApi
   fun fetchArticles(language: String, category: String): Flow<PagingData<Article>> {
            val pagingSourceFactory =
                { database.articleDao().getNews(language, category) }
            return Pager(
                config = PagingConfig(NETWORK_PAGE_SIZE, maxSize = 300, enablePlaceholders = true),
                remoteMediator = NewssterRemoteMediator(language, category, service, database),
                pagingSourceFactory = pagingSourceFactory
            ).flow
    }

    fun getArticle(id: String) = flow {
        val article = database.articleDao().getNewsById(id)
        emit(article)
    }.flowOn(Dispatchers.Default)

    companion object {
        private const val NETWORK_PAGE_SIZE = 80
    }
}