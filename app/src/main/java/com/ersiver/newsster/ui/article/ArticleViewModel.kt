package com.ersiver.newsster.ui.article

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.ersiver.newsster.model.Article
import com.ersiver.newsster.repository.NewssterRepository
import com.ersiver.newsster.util.SingleEvent

class ArticleViewModel @ViewModelInject constructor(
    private val repository: NewssterRepository
) : ViewModel() {

    private val _articleId = MutableLiveData<String>()

    private val _article = _articleId.switchMap { articleId ->
        repository.getArticle(articleId)
    }

    val article: LiveData<Article> get() = _article

    private val _shareArticleEvent = MutableLiveData<SingleEvent<String>>()
    val shareArticleEvent: LiveData<SingleEvent<String>>
        get() = _shareArticleEvent

    private val _openWebsiteEvent = MutableLiveData<SingleEvent<String>>()
    val openWebsiteEvent: LiveData<SingleEvent<String>>
        get() = _openWebsiteEvent


    fun start(articleId: String) {
        _articleId.value = articleId
    }

    fun shareArticle(articleUrl: String) {
        _shareArticleEvent.value = SingleEvent(articleUrl)
    }

    fun openWebsite(articleUrl: String) {
        _openWebsiteEvent.value = SingleEvent(articleUrl)
    }

}
