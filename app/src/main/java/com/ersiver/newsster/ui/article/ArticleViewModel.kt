package com.ersiver.newsster.ui.article

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.ersiver.newsster.model.Article
import com.ersiver.newsster.repository.NewssterRepository
import com.ersiver.newsster.util.SingleEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ArticleViewModel @Inject constructor(
    private val repository: NewssterRepository
) : ViewModel() {

    private val _openWebsiteEvent = MutableLiveData<SingleEvent<Article>>()
    private val _shareArticleEvent = MutableLiveData<SingleEvent<Article>>()
    var articleLiveData: LiveData<Article>? = null

    val shareArticleEvent: LiveData<SingleEvent<Article>>
        get() = _shareArticleEvent

    val openWebsiteEvent: LiveData<SingleEvent<Article>>
        get() = _openWebsiteEvent

    fun getArticle(id: String): LiveData<Article> {
        return articleLiveData ?: liveData {
            emit(repository.getArticle(id))
        }.also {
            articleLiveData = it
        }
    }

    fun shareArticle() {
        articleLiveData?.value?.let {
            _shareArticleEvent.value = SingleEvent(it)
        }
    }

    fun openWebsite() {
        articleLiveData?.value?.let {
            _openWebsiteEvent.value = SingleEvent(it)
        }
    }
}