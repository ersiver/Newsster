package com.ersiver.newsster.ui.article

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ersiver.newsster.MainCoroutinesRule
import com.ersiver.newsster.api.NewssterService
import com.ersiver.newsster.db.NewssterDatabase
import com.ersiver.newsster.repository.NewssterRepository
import com.ersiver.newsster.utils.TestUtil
import com.ersiver.newsster.utils.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.*

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class ArticleViewModelTest {

    @get:Rule
    var coroutinesRule = MainCoroutinesRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var repository: NewssterRepository
    private lateinit var viewModel: ArticleViewModel
    private val article = TestUtil.createArticle()
    private val database = mock(NewssterDatabase::class.java, RETURNS_DEEP_STUBS)
    private val service = mock(NewssterService::class.java)

    @Before
    fun setup() = runBlockingTest {
        repository = NewssterRepository(service, database)
        viewModel = ArticleViewModel(repository)

        `when`(database.articleDao().getNewsById("TEST_ID")).thenReturn(article)
        viewModel.getArticle(id = "TEST_ID")
    }

    @Test
    fun fetchArticleTest() = coroutinesRule.runBlockingTest {
        val value = viewModel.articleLiveData?.getOrAwaitValue()
        assertThat(value?.id, `is`("TEST_ID"))
        assertThat(value?.title, `is`(article.title))
        assertThat(value?.description, `is`(article.description))
        assertThat(value?.author, `is`(article.author))
        assertThat(value?.url, `is`(article.url))
        assertThat(value?.source?.name, `is`(article.source.name))
        assertThat(value?.imgUrl, `is`(article.imgUrl))
        assertThat(value?.category, `is`(article.category))
        assertThat(value?.language, `is`(article.language))
        assertThat(value?.date, `is`(article.date))
    }

    @Test
    fun shareArticleTest() = runBlockingTest {
        viewModel.articleLiveData?.getOrAwaitValue()
        viewModel.shareArticle()
        val event = viewModel.shareArticleEvent.getOrAwaitValue()
        assertThat(event.getContentIfNotHandled()?.id, `is`("TEST_ID"))

    }

    @Test
    fun openWebsiteTest() {
        viewModel.articleLiveData?.getOrAwaitValue()
        viewModel.openWebsite()
        val event = viewModel.openWebsiteEvent.getOrAwaitValue()
        assertThat(event.getContentIfNotHandled()?.id, `is`("TEST_ID"))
    }
}