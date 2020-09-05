package com.ersiver.newsster.api

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ersiver.newsster.MainCoroutinesRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException

@ExperimentalCoroutinesApi
class NewssterServiceTest : ApiAbstract<NewssterService>() {
    @get: Rule
    val coroutinesRule = MainCoroutinesRule()

    @get: Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var service: NewssterService

    @Before
    fun initService() {
        service = createService(NewssterService::class.java)
    }

    @Throws(IOException::class)
    @Test
    fun getNewsFromNetwork() = runBlocking {
        enqueueResponse("/NewssterResponse.json")
        val response = (service.getNews())
        val responseBody = requireNotNull(response.articleDTO)
        mockWebServer.takeRequest()

        val loaded = responseBody[0]
        assertThat(responseBody.count(), `is`(20))
        assertThat(loaded.author, `is`("https://www.facebook.com/bbcnews"))
        assertThat(
            loaded.title,
            `is`("I'm A Celebrity will swap jungle for ruined British castle.")
        )
        assertThat(loaded.url, `is`("https://www.bbc.co.uk/news/entertainment-arts-53693651"))
        assertThat(
            loaded.imgUrl,
            `is`("https://ichef.bbci.co.uk/news/1024/branded_news/FA36/production/_113845046_jossa_shutterstock_2.jpg")
        )
        assertThat(
            loaded.description,
            `is`("The reality show will swap the Australian jungle for a ruined British castle for this year's series.")
        )
        assertThat(loaded.date, `is`("2020-08-07T10:18:03Z"))
        assertThat(
            loaded.content,
            `is`("Image copyrightITV/ShutterstockImage caption Jacqueline Jossa doing a bush tucker trial on her way to winning the 2019 series [+2602 chars]")
        )
        assertThat(loaded.source.name, `is`("BBC News"))
    }
}
