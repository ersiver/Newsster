package com.ersiver.newsster.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ersiver.newsster.MainCoroutinesRule
import com.ersiver.newsster.api.NewssterService
import com.ersiver.newsster.db.ArticleDao
import com.ersiver.newsster.db.NewssterDatabase
import com.ersiver.newsster.utils.TestUtil
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

/**
 * Unit tests for the implementation of [NewssterRepository]
 */
@ExperimentalCoroutinesApi
class NewssterRepositoryTest {
    @get:Rule
    val mainCoroutinesRule = MainCoroutinesRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var repository: NewssterRepository
    private lateinit var database : NewssterDatabase
    private lateinit var service: NewssterService

    @Before
    fun setUp() {
        database = mock(NewssterDatabase::class.java)
        service = mock(NewssterService::class.java)
        repository = NewssterRepository(service, database)
    }

    @Test
    fun getArticle_flowEmitsArticle() = runBlocking {
        //Stub out database to return a mock dao.
        val dao = mock(ArticleDao::class.java)
        `when`(database.articleDao()).thenReturn(dao)

        //Stub out dao to return an Article.
        val article = TestUtil.createArticle()
        `when`(dao.getNewsById("TEST_ID")).thenReturn(article)

        //Method under test.
        val flow = repository.getArticle("TEST_ID")

        //Verify data in the result.
        flow.collect { result ->
            assertThat(result, `is`(article))
        }
    }
}