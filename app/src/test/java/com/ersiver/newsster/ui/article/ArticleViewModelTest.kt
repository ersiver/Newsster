package com.ersiver.newsster.ui.article

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ersiver.newsster.MainCoroutinesRule
import com.ersiver.newsster.repository.NewssterRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock

@ExperimentalCoroutinesApi
class ArticleViewModelTest {
    @ExperimentalCoroutinesApi
    @get:Rule
    var coroutinesRule = MainCoroutinesRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    lateinit var repository: NewssterRepository
    lateinit var viewModel: ArticleViewModel

    @Before
    fun setup() {
        repository = mock(NewssterRepository::class.java)
        viewModel = ArticleViewModel(repository)
    }

    @Test
    fun getArticle()  {

    }

    @Test
    fun getShareArticleEvent() {
    }

    @Test
    fun getOpenWebsiteEvent() {
    }

    @Test
    fun start() {

    }

    @Test
    fun shareArticle() {
    }

    @Test
    fun openWebsite() {
    }
}
