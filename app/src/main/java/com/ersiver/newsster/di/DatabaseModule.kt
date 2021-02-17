package com.ersiver.newsster.di

import android.app.Application
import androidx.room.Room
import com.ersiver.newsster.db.ArticleDao
import com.ersiver.newsster.db.NewssterDatabase
import com.ersiver.newsster.db.RemoteKeyDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(app: Application): NewssterDatabase {
        return Room
            .databaseBuilder(app, NewssterDatabase::class.java, "newsster_db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideArticleDao(newssterDatabase: NewssterDatabase): ArticleDao{
        return newssterDatabase.articleDao()
    }

    @Provides
    fun provideRemoteKeysDao(newssterDatabase: NewssterDatabase): RemoteKeyDao{
        return newssterDatabase.remoteKeyDao()
    }
}