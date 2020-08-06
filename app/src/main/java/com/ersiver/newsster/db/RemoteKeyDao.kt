package com.ersiver.newsster.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RemoteKeyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(keys: List<RemoteKey>)

    @Query("SELECT * FROM remote_keys WHERE articleId = :articleId")
    suspend fun remoteKeyByArticle(articleId: String): RemoteKey

    @Query("DELETE FROM remote_keys")
    suspend fun clearRemoteKeys()
}