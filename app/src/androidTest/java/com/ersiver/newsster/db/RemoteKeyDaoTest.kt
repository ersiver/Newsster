package com.ersiver.newsster.db

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.ersiver.newsster.utils.TestUtil
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@SmallTest
@RunWith(AndroidJUnit4::class)
class RemoteKeyDaoTest : LocalDatabase() {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var remoteKeyDao: RemoteKeyDao

    @Before
    fun setup() {
        remoteKeyDao = database.remoteKeyDao()
    }

    @Test
    fun insertAndLoadById() = runBlockingTest {
        val article = TestUtil.createArticle()
        val remoteKey = TestUtil.createRemoteKey()
        val remoteKeys = listOf(remoteKey)
        remoteKeyDao.insertAll(remoteKeys)

        val loaded = remoteKeyDao.remoteKeyByArticle(article.id)

        assertThat(loaded, `is`(notNullValue()))
        assertThat(loaded.articleId, `is`(article.id))
        assertThat(loaded.nextKey, `is`(remoteKey.nextKey))
        assertThat(loaded.prevKey, `is`(remoteKey.prevKey))
    }

    @Test
    fun insertAndDelete() = runBlockingTest {
        val article = TestUtil.createArticle()
        val remoteKey = TestUtil.createRemoteKey()
        val remoteKeys = listOf(remoteKey)
        remoteKeyDao.insertAll(remoteKeys)

        remoteKeyDao.clearRemoteKeys()

        val load = remoteKeyDao.remoteKeyByArticle(article.id)
        assertThat(load, `is`(nullValue()))
    }
}