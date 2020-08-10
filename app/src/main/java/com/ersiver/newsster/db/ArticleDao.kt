package com.ersiver.newsster.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ersiver.newsster.model.Article

@Dao
interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(news: List<Article>)

    @Query("SELECT * FROM article_table WHERE language = :language AND category = :category")
    fun getNews(language: String, category: String): PagingSource<Int, Article>

    @Query("DELETE FROM article_table")
    suspend fun clearNews()

    @Query("SELECT * FROM article_table WHERE id = :id")
    suspend fun getNewsById(id: String): Article
}