package com.ersiver.newsster.di

import com.ersiver.newsster.api.NewssterService
import com.ersiver.newsster.db.NewssterDatabase
import com.ersiver.newsster.repository.NewssterRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideNewssterRepository(
        service: NewssterService,
        database: NewssterDatabase
    ): NewssterRepository {
        return NewssterRepository(service, database)
    }
}