package com.ersiver.newsster.db

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.ersiver.newsster.model.Article
import com.ersiver.newsster.utils.TestUtil
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@SmallTest
@RunWith(AndroidJUnit4::class)
class ArticleDaoTest : LocalDatabase() {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var articleDao: ArticleDao

    @Before
    fun setUp() {
        articleDao = database.articleDao()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndLoad() = runBlockingTest {
        val article = TestUtil.createArticle()
        val news = listOf(article)
        articleDao.insertAll(news)

        val loaded = articleDao.getNews(article.category, article.language)

        assertThat(loaded, notNullValue())
    }

    @Test
    @Throws(Exception::class)
    fun insertAndLoadById() = runBlockingTest {
        val article = TestUtil.createArticle()
        val news = listOf(article)
        articleDao.insertAll(news)

        val loaded = articleDao.getNewsById(article.id)

        assertThat(loaded.id, `is`(article.id))
        assertThat(loaded.url, `is`(article.url))
        assertThat(loaded.author, `is`(article.author))
        assertThat(loaded.title, `is`(article.title))
        assertThat(loaded.description, `is`(article.description))
        assertThat(loaded.imgUrl, `is`(article.imgUrl))
        assertThat(loaded.content, `is`(article.content))
        assertThat(loaded.source.name, `is`(article.source.name))
        assertThat(loaded.category, `is`(article.category))
        assertThat(loaded.language, `is`(article.language))
        assertThat(loaded.date, `is`(article.date))
    }

    @Test
    @Throws(Exception::class)
    fun insertAndDelete() = runBlockingTest {
        val article = TestUtil.createArticle()
        val news = listOf(article)
        articleDao.insertAll(news)

        var loaded: Article = articleDao.getNewsById(article.id)
        assertThat(loaded, notNullValue())

        articleDao.clearNews()
        loaded = articleDao.getNewsById(article.id)
        assertThat(loaded, `is`(nullValue()))
    }
}