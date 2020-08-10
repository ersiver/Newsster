package com.ersiver.newsster.ui.article

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.ersiver.newsster.model.Article
import com.ersiver.newsster.repository.NewssterRepository
import com.ersiver.newsster.util.SingleEvent

class ArticleViewModel @ViewModelInject constructor(
    private val repository: NewssterRepository
) : ViewModel() {

    private val _articleId = MutableLiveData<String>()

    val articleLiveData: LiveData<Article>

    init {
        articleLiveData = _articleId.switchMap {articleId ->
            repository.getArticle(articleId).asLiveData()
        }
    }

    private val _shareArticleEvent = MutableLiveData<SingleEvent<String>>()
    val shareArticleEvent: LiveData<SingleEvent<String>>
        get() = _shareArticleEvent

    private val _openWebsiteEvent = MutableLiveData<SingleEvent<String>>()
    val openWebsiteEvent: LiveData<SingleEvent<String>>
        get() = _openWebsiteEvent


    fun fetchArticle(id: String) {
        _articleId.value = id
    }

    fun shareArticle(articleUrl: String) {
        _shareArticleEvent.value = SingleEvent(articleUrl)
    }

    fun openWebsite(articleUrl: String) {
        _openWebsiteEvent.value = SingleEvent(articleUrl)
    }
}
