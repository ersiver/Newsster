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
import org.hamcrest.CoreMatchers.notNullValue
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
    fun setup() {
        repository = NewssterRepository(service, database)
        viewModel = ArticleViewModel(repository)
    }

    @Test
    fun fetchArticleTest() = runBlockingTest {
        `when`(database.articleDao().getNewsById("_testId")).thenReturn(article)

        viewModel.fetchArticle(id = "_testId")

        assertThat(viewModel.articleLiveData.getOrAwaitValue().id, `is`(article.id))
        assertThat(viewModel.articleLiveData.getOrAwaitValue().title, `is`(article.title))
        assertThat(
            viewModel.articleLiveData.getOrAwaitValue().description,
            `is`(article.description)
        )
        assertThat(viewModel.articleLiveData.getOrAwaitValue().author, `is`(article.author))
        assertThat(viewModel.articleLiveData.getOrAwaitValue().url, `is`(article.url))
        assertThat(
            viewModel.articleLiveData.getOrAwaitValue().source.name,
            `is`(article.source.name)
        )
        assertThat(viewModel.articleLiveData.getOrAwaitValue().imgUrl, `is`(article.imgUrl))
        assertThat(viewModel.articleLiveData.getOrAwaitValue().category, `is`(article.category))
        assertThat(viewModel.articleLiveData.getOrAwaitValue().language, `is`(article.language))
        assertThat(viewModel.articleLiveData.getOrAwaitValue().date, `is`(article.date))
    }

    @Test
    fun shareArticleTest() {
        viewModel.shareArticle(article.id)
        val event = viewModel.shareArticleEvent.getOrAwaitValue()
        assertThat(event.getContentIfNotHandled(), `is`(notNullValue()))
    }

    @Test
    fun openWebsiteTest() {
        viewModel.openWebsite(article.url)
        val event = viewModel.openWebsiteEvent.getOrAwaitValue()
        assertThat(event.getContentIfNotHandled(), `is`(notNullValue()))
    }
}