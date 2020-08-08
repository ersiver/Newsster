package com.ersiver.newsster.db

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import org.junit.After
import org.junit.Before

import org.junit.Assert.*
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
abstract class LocalDatabase {

    lateinit var database: NewssterDatabase

    @Before
    fun setUpDatabase() {
        database = Room.inMemoryDatabaseBuilder(
            getApplicationContext(),
            NewssterDatabase::class.java
        ).allowMainThreadQueries()
            .build()
    }

    @After
    fun tearDown() {
        database.close()
    }
}