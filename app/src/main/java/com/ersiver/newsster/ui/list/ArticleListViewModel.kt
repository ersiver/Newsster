package com.ersiver.newsster.ui.list

import androidx.lifecycle.*
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ersiver.newsster.model.Article
import com.ersiver.newsster.repository.NewssterRepository
import com.ersiver.newsster.util.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * ViewModel for the [ArticleListFragment] screen.
 * The ViewModel works with the [NewssterRepository]
 * to get the list of articles.
 */
@ExperimentalPagingApi
@HiltViewModel
class ArticleListViewModel @Inject constructor(
    private val repository: NewssterRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var currentNews: Flow<PagingData<Article>>? = null
    private val _categoryLocalizedLiveData: MutableLiveData<Int> =
        MutableLiveData(getLastSavedLocalizedCategory())
    private val _categoryLiveData: MutableLiveData<String> = MutableLiveData(getLastSavedCategory())
    private val _languageLiveData: MutableLiveData<String> = MutableLiveData(getLastSavedLanguage())

    val categoryLocalLiveData: LiveData<Int> get() = _categoryLocalizedLiveData
    val categoryLiveData: LiveData<String> get() = _categoryLiveData
    val languageLiveData: LiveData<String> get() = _languageLiveData

    fun loadNews(): Flow<PagingData<Article>> {
        val category = _categoryLiveData.value!!
        val language = _languageLiveData.value!!

        val lastResult = currentNews
        if (lastResult != null && !shouldRefresh(language, category))
            return lastResult

        val newNews =
            repository.fetchArticles(language, category).cachedIn(viewModelScope)
        currentNews = newNews

        //Save new filters after checks are made to establish, if the news should be refreshed.
        saveCategoryFiltering(category)
        saveLanguageFiltering(language)
        saveCategoryLocalized()

        return newNews
    }

    private fun shouldRefresh(language: String, category: String): Boolean {
        return category != getLastSavedCategory()
                || language != getLastSavedLanguage()
    }

    fun getLastSavedCategory() = savedStateHandle
        .getLiveData<String>(
            SAVED_STATE_CATEGORY
        ).value ?: DEFAULT_STATE_CATEGORY

    fun getLastSavedLanguage() = savedStateHandle
        .getLiveData<String>(
            SAVED_STATE_LANGUAGE
        ).value ?: DEFAULT_LANGUAGE

    private fun getLastSavedLocalizedCategory() = savedStateHandle
        .getLiveData<Int>(
            SAVED_STATE_LOCAL_TITLE
        ).value ?: DEFAULT_TITLE

    fun updateCategory(category: String, categoryLocalized: Int) {
        _categoryLiveData.value = category
        _categoryLocalizedLiveData.value = categoryLocalized
    }

    fun updateLanguage(language: String) {
        _languageLiveData.value = language
    }

    fun saveCategoryFiltering(category: String) {
        savedStateHandle.set(SAVED_STATE_CATEGORY, category)
    }

    fun saveLanguageFiltering(language: String) {
        savedStateHandle.set(SAVED_STATE_LANGUAGE, language)
    }

    private fun saveCategoryLocalized() {
        _categoryLocalizedLiveData.value?.let {
            savedStateHandle.set(SAVED_STATE_LOCAL_TITLE, it)
        }
    }
}