package com.ersiver.newsster.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.ersiver.newsster.api.NewssterResponse
import com.ersiver.newsster.api.NewssterService
import com.ersiver.newsster.api.asModel
import com.ersiver.newsster.db.NewssterDatabase
import com.ersiver.newsster.db.RemoteKey
import com.ersiver.newsster.model.Article
import com.ersiver.newsster.util.EspressoUriIdlingResource
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException
import java.io.InvalidObjectException
import javax.inject.Inject
import javax.inject.Singleton

/**
 * RemoteMediator for a DB + Network based PagingData stream, which
 * triggers network requests to fetch additional items when a user
 * scrolls to the end of the list of items stored in DB.
 */
@ExperimentalPagingApi
@Singleton
class NewssterRemoteMediator @Inject constructor(
    private val language: String,
    private val category: String,
    private val service: NewssterService,
    private val database: NewssterDatabase
) : RemoteMediator<Int, Article>() {

    private val remoteKeyDao = database.remoteKeyDao()
    private val articleDao = database.articleDao()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Article>
    ): MediatorResult {
        try {
            val loadKey: Int = when (loadType) {
                LoadType.REFRESH -> {
                    Timber.i("REFRESH")
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.nextKey?.minus(1) ?: STARTING_PAGE
                }

                LoadType.PREPEND -> {
                    Timber.i("PREPEND")
                    val remoteKey = getRemoteKeyForFirstItem(state)
                        ?: throw InvalidObjectException("Something went wrong.")
                    remoteKey.prevKey
                        ?: return MediatorResult.Success(endOfPaginationReached = true)
                    remoteKey.prevKey
                }

                LoadType.APPEND -> {
                    Timber.i("APPEND")

                    val remoteKey = getRemoteKeyForLastItem(state)
                    if (remoteKey?.nextKey == null)
                        throw InvalidObjectException("Something went wrong")
                    remoteKey.nextKey
                }
            }

            // Suspending network load via Retrofit. This doesn't need to
            // be wrapped in a withContext(Dispatcher.IO) { ... } block
            // since Retrofit's Coroutine CallAdapter dispatches on a
            // worker thread.
            val apiResponse = newssterResponse(loadKey, state)
            val news = apiResponse.asModel()
            val endOfPaginationReached = news.isEmpty()

            // Store loaded data, and next key in transaction, so that
            // they're always consistent.
            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    remoteKeyDao.clearRemoteKeys()
                    articleDao.clearNews()
                }

                val prevKey = if (loadKey == STARTING_PAGE) null else loadKey - 1
                val nextKey = if (endOfPaginationReached) null else loadKey + 1
                val keys = news.map { article ->
                    RemoteKey(articleId = article.id, nextKey = nextKey, prevKey = prevKey)
                }

                for (article in news) {
                    article.language = language
                    article.category = category
                }

                articleDao.insertAll(news)
                remoteKeyDao.insertAll(keys)
            }

            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: IOException) {
            return MediatorResult.Error(e)
        } catch (e: HttpException) {
            return MediatorResult.Error(e)
        }
    }

    //EspressoUriIdlingResource implemented for testing purpose.
    private suspend fun newssterResponse(
        loadKey: Int,
        state: PagingState<Int, Article>
    ): NewssterResponse {
        EspressoUriIdlingResource.beginLoad()
        try {
            return service.getNews(
                country = language,
                category = category,
                page = loadKey,
                pageSize = state.config.pageSize
            )
        } finally {
            EspressoUriIdlingResource.endLoad()
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, Article>): RemoteKey? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { articleId ->
                remoteKeyDao.remoteKeyByArticle(articleId)
            }
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, Article>): RemoteKey? {
        return state.pages.lastOrNull {
            it.data.isNotEmpty()
        }?.data?.lastOrNull()?.let { lastArticle ->
            remoteKeyDao.remoteKeyByArticle(lastArticle.id)
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, Article>): RemoteKey? {
        return state.pages.firstOrNull {
            it.data.isNotEmpty()
        }?.data?.firstOrNull()?.let { firstArticle ->
            remoteKeyDao.remoteKeyByArticle(firstArticle.id)
        }
    }

    companion object {
        private const val STARTING_PAGE = 1
    }
}