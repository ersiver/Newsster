package com.ersiver.newsster.utils

import com.ersiver.newsster.db.RemoteKey
import com.ersiver.newsster.model.Article
import com.ersiver.newsster.model.Source

object TestUtil {

    fun createArticle() = Article(
        id = "TEST_ID",
        url = "https://www.bbc.co.uk/news/uk-england-stoke-staffordshire-53690521",
        author = "https://www.facebook.com/bbcnews",
        title = "Stone pub landlord 'complacent' in enforcing Covid rules - BBC News",
        description = "The landlord of the Crown and Anchor says he \"regrets\" not taking a tougher stance with customers.",
        imgUrl = "https://ichef.bbci.co.uk/news/1024/branded_news/8943/production/_113693153_img_0683.jpg",
        content = "Image caption\r\n The owner of the Crown and Anchor, Custodio Pinto, said he regretted what happened\r\nThe landlord of a pub linked",
        source = createSource(),
        category = "",
        language = "gb",
        date = "07-08-2020"
    )

    private fun createSource() = Source(
        name = "BBC News"
    )

    fun createRemoteKey() = RemoteKey(
        createArticle().id,
        100,
        200
    )
}