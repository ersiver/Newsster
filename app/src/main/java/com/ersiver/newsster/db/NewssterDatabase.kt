package com.ersiver.newsster.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ersiver.newsster.model.Article
import com.ersiver.newsster.model.SourceConverter

@Database(entities = [Article::class, RemoteKey::class], version = 1, exportSchema = false)
@TypeConverters(value = [SourceConverter::class])
abstract class NewssterDatabase : RoomDatabase(){
    abstract fun articleDao(): ArticleDao
    abstract fun remoteKeyDao(): RemoteKeyDao
}